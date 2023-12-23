package groupB.newbankV5.paymentgateway.components;

import groupB.newbankV5.paymentgateway.config.KafkaProducerService;
import groupB.newbankV5.paymentgateway.connectors.MockBankProxy;
import groupB.newbankV5.paymentgateway.connectors.dto.TransactionDto;
import groupB.newbankV5.paymentgateway.entities.*;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentgateway.interfaces.IMockBank;
import groupB.newbankV5.paymentgateway.interfaces.IPaymentProcessor;
import groupB.newbankV5.paymentgateway.interfaces.ITransactionConfirmation;

import groupB.newbankV5.paymentgateway.interfaces.ITransactionFinder;
import groupB.newbankV5.paymentgateway.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class TransactionConfirmator implements ITransactionConfirmation {

    private static final Logger log = Logger.getLogger(TransactionConfirmator.class.getName());
    private final IPaymentProcessor paymentProcessor;
    private final TransactionRepository transactionRepository;
    private final IMockBank mockBankProxy;

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public TransactionConfirmator(TransactionRepository transactionRepository, IPaymentProcessor paymentProcessor, MockBankProxy mockBankProxy, KafkaProducerService kafkaProducerService) {
        this.transactionRepository = transactionRepository;
        this.paymentProcessor = paymentProcessor;
        this.mockBankProxy = mockBankProxy;
        this.kafkaProducerService = kafkaProducerService;
    }


    @Override
    public String confirmPayment(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId);
        if (transaction != null && transaction.getStatus().equals(TransactionStatus.AUTHORIZED)) {
            transaction.setStatus(TransactionStatus.CONFIRMED);
            transactionRepository.save(transaction);

            CreditCard usedCreditCard = transaction.getCreditCard();
            if(transaction.getBank().equals("NewBank")) {
                log.info("\u001B[32msend fund reservation request\u001B[0m");
                paymentProcessor.reserveFunds(TransactionDto.fromTransaction(transaction));
            }
            else
                mockBankProxy.reserveFunds(Double.parseDouble(transaction.getAmount()), usedCreditCard.getCardNumber(), usedCreditCard.getExpiryDate(), usedCreditCard.getCvv());
            transaction.setStatus(TransactionStatus.PENDING_SETTLEMENT);
            transactionRepository.save(transaction);
            kafkaProducerService.sendMessage(transaction);
            return "Payment confirmed";
        }
        if (transaction == null)
            return "Transaction not found, try again";
        return "Payment already confirmed";

    }

}