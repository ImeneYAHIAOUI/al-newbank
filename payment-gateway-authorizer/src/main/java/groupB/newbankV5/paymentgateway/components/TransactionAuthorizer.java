package groupB.newbankV5.paymentgateway.components;

import groupB.newbankV5.paymentgateway.config.KafkaProducerService;
import groupB.newbankV5.paymentgateway.connectors.BusinessIntegratorProxy;
import groupB.newbankV5.paymentgateway.connectors.CreditCardNetworkProxy;
import groupB.newbankV5.paymentgateway.connectors.PaymentProcessor;
import groupB.newbankV5.paymentgateway.connectors.dto.ApplicationDto;
import groupB.newbankV5.paymentgateway.connectors.dto.CcnResponseDto;
import groupB.newbankV5.paymentgateway.connectors.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentgateway.controllers.dto.MerchantDto;
import groupB.newbankV5.paymentgateway.entities.*;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.CCNException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentgateway.interfaces.*;

import groupB.newbankV5.paymentgateway.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Logger;

@Service
public class TransactionAuthorizer implements ITransactionProcessor, ITransactionFinder {

    private static final Logger log = Logger.getLogger(TransactionAuthorizer.class.getName());
    private final CreditCardNetworkProxy creditCardNetworkProxy;
    private final BusinessIntegratorProxy businessIntegratorProxy;
    private final TransactionRepository transactionRepository;
    private final IRSA rsa;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final long TIMEOUT_MS = 4000;

    private final PaymentProcessor paymentProcessor;

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public TransactionAuthorizer(
            CreditCardNetworkProxy creditCardNetworkProxy, IRSA rsa, BusinessIntegratorProxy businessIntegratorProxy, TransactionRepository transactionRepository, PaymentProcessor paymentProcessor, KafkaProducerService kafkaProducerService) {
        this.creditCardNetworkProxy = creditCardNetworkProxy;
        this.businessIntegratorProxy=businessIntegratorProxy;
        this.rsa = rsa;
        this.transactionRepository = transactionRepository;
        this.paymentProcessor = paymentProcessor;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public long getAuthorizedTransaction(Long merchantId) {
        long confirmedTransactionsCount = transactionRepository.findByStatus(TransactionStatus.AUTHORIZED)
                .stream()
                .filter(transaction -> merchantId.equals(transaction.getMerchantId()))
                .count();
        return confirmedTransactionsCount;
    }


    @Override
    public Transaction processPayment(String token, double amount, String cryptedCreditCard) throws InvalidTokenException,
            ApplicationNotFoundException, CCNException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException, InvalidKeySpecException, ExecutionException, InterruptedException, TimeoutException {

        Future<Transaction> future = executorService.submit(() -> {
        ApplicationDto application = businessIntegratorProxy.validateToken(token);
        MerchantDto merchant = application.getMerchant();
        CreditCard creditCard = rsa.decryptPaymentRequestCreditCard(cryptedCreditCard, application);


        log.info("\u001B[32mSending payment authorization request to CCN\u001B[0m");

        CcnResponseDto ccnResponseDto = creditCardNetworkProxy.authorizePayment(
                new PaymentDetailsDTO(creditCard.getCardNumber(), creditCard.getExpiryDate(), creditCard.getCvv(), amount)
        );
        if (!ccnResponseDto.isApproved()) {
            throw new CCNException("\u001B[31mPayment not authorized\u001B[0m");
        }
        Transaction transaction = new Transaction(merchant.getBankAccount(), ccnResponseDto.getAuthToken(), String.valueOf(amount));
        transaction.setId(UUID.randomUUID());
        transaction.setExternal(true);
        transaction.setApplicationId(application.getId());
        transaction.setCreditCardType(ccnResponseDto.getCardType());
        transaction.setMerchantId(merchant.getId());
        CreditCard card = new CreditCard(creditCard.getCardNumber(), creditCard.getExpiryDate(), creditCard.getCvv());
        transaction.setCreditCard(card);
        transaction.setSender(new BankAccount(ccnResponseDto.getAccountIBAN(),ccnResponseDto.getAccountBIC()));
        transaction.setRecipient(merchant.getBankAccount());
        transaction.setStatus(TransactionStatus.AUTHORIZED);
        transaction.setBank(ccnResponseDto.getBankName());
        log.info("\u001B[32mPayment authorized\u001B[0m");

        transactionRepository.save(transaction);
        return transaction;
                }
        );
        return future.get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void saveFailedTransaction(String token, double amount, String cryptedCreditCard) {
        Transaction t = new Transaction();
        t.setId(UUID.randomUUID());
        t.setAmount(String.valueOf(amount));
        t.setStatus(TransactionStatus.FAILED);
        paymentProcessor.saveTransactions(new Transaction[]{t});
    }


}