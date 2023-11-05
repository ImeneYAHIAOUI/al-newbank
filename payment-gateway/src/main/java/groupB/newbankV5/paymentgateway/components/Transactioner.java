package groupB.newbankV5.paymentgateway.components;

import groupB.newbankV5.paymentgateway.config.KafkaProducerService;
import groupB.newbankV5.paymentgateway.connectors.BusinessIntegratorProxy;
import groupB.newbankV5.paymentgateway.connectors.CreditCardNetworkProxy;
import groupB.newbankV5.paymentgateway.connectors.dto.ApplicationDto;
import groupB.newbankV5.paymentgateway.connectors.dto.CcnResponseDto;
import groupB.newbankV5.paymentgateway.connectors.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentgateway.entities.*;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.CCNException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentgateway.interfaces.IRSA;
import groupB.newbankV5.paymentgateway.interfaces.ITransactionProcessor;

import groupB.newbankV5.paymentgateway.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class Transactioner implements ITransactionProcessor {

    private static final Logger log = Logger.getLogger(Transactioner.class.getName());
    private CreditCardNetworkProxy creditCardNetworkProxy;
    private BusinessIntegratorProxy businessIntegratorProxy;
    private TransactionRepository transactionRepository;
    private IRSA rsa;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    public Transactioner(
                         CreditCardNetworkProxy creditCardNetworkProxy, IRSA rsa,BusinessIntegratorProxy businessIntegratorProxy, TransactionRepository transactionRepository) {
        this.creditCardNetworkProxy = creditCardNetworkProxy;
        this.businessIntegratorProxy=businessIntegratorProxy;
        this.rsa = rsa;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public Transaction processPayment(String token, BigDecimal amount, String cryptedCreditCard) throws InvalidTokenException,
            ApplicationNotFoundException, CCNException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        ApplicationDto application = businessIntegratorProxy.validateToken(token);
        log.info("token validated");
        Merchant merchant = application.getMerchant();
        log.info("encrypted credit card: " + cryptedCreditCard);

        CreditCard creditCard = rsa.decryptPaymentRequestCreditCard(cryptedCreditCard, application);
        log.info("successfully decrypted credit card");

        CcnResponseDto ccnResponseDto = creditCardNetworkProxy.authorizePayment(
                new PaymentDetailsDTO(creditCard.getCardNumber(), creditCard.getExpiryDate(), creditCard.getCvv(), amount)
        );
        if (!ccnResponseDto.isApproved()) {
            throw new CCNException("Payment not authorized");
        }
        Transaction transaction = new Transaction(merchant.getBankAccount(), ccnResponseDto.getAuthToken(), amount);
        transaction.setId(UUID.randomUUID());
        transaction.setExternal(true);
        transaction.setCreditCardType(ccnResponseDto.getCardType());
        transaction.setSender(new BankAccount(ccnResponseDto.getAccountIBAN(),ccnResponseDto.getAccountBIC()));
        transaction.setRecipient(merchant.getBankAccount());
        transaction.setStatus(TransactionStatus.AUTHORIZED);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Override
    public String confirmPayment(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction != null) {
            transaction.setStatus(TransactionStatus.PENDING_SETTLEMENT);
            transactionRepository.save(transaction);
            kafkaProducerService.sendMessage(transaction);
            return "Payment confirmed";
        }
        return "Transaction not found, try again";
    }
}
