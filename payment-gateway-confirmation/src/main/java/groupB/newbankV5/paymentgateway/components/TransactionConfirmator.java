package groupB.newbankV5.paymentgateway.components;

import groupB.newbankV5.paymentgateway.config.KafkaProducerService;
import groupB.newbankV5.paymentgateway.connectors.MockBankProxy;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Service
public class TransactionConfirmator implements ITransactionConfirmation, ITransactionFinder {

    private static final Logger log = Logger.getLogger(TransactionConfirmator.class.getName());
    private final IPaymentProcessor paymentProcessor;
    private final TransactionRepository transactionRepository;
    private final IMockBank mockBankProxy;

    private final KafkaProducerService kafkaProducerService;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final long TIMEOUT_MS = 4000;


    @Autowired
    public TransactionConfirmator(TransactionRepository transactionRepository, IPaymentProcessor paymentProcessor, MockBankProxy mockBankProxy, KafkaProducerService kafkaProducerService) {
        this.transactionRepository = transactionRepository;
        this.paymentProcessor = paymentProcessor;
        this.mockBankProxy = mockBankProxy;
        this.kafkaProducerService = kafkaProducerService;
    }
    @Override
    public long getConfirmedTransaction(Long merchantId) throws InvalidTokenException, ApplicationNotFoundException {
        long confirmedTransactionsCount = transactionRepository.findByStatus(TransactionStatus.CONFIRMED)
                .stream()
                .filter(transaction -> merchantId.equals(transaction.getMerchantId()))
                .count();
        return confirmedTransactionsCount;
    }

    @Override
    public String confirmPayment(UUID transactionId) throws ExecutionException, InterruptedException, TimeoutException {
        Future<String> future = executorService.submit(() -> {
                Transaction transaction = transactionRepository.findById(transactionId);
            if (transaction != null && transaction.getStatus().equals(TransactionStatus.AUTHORIZED)) {
                transaction.setStatus(TransactionStatus.CONFIRMED);
                transactionRepository.save(transaction);

                CreditCard usedCreditCard = transaction.getCreditCard();
                if(transaction.getBank().equals("NewBank")) {
                    log.info("\u0011[32msend fund reservation request\u001B[0m");
                    paymentProcessor.reserveFunds(transaction);
                }
                else
                    mockBankProxy.reserveFunds(transaction.getAmount(), usedCreditCard.getCardNumber(), usedCreditCard.getExpiryDate(), usedCreditCard.getCvv());
                transaction.setStatus(TransactionStatus.PENDING_SETTLEMENT);
                transactionRepository.save(transaction);
                kafkaProducerService.sendMessage(transaction);
                return "Payment confirmed";
            }
            if (transaction == null)
                return "Transaction not found, try again";
            return "Payment already confirmed";
                });
        return future.get(TIMEOUT_MS, TimeUnit.MILLISECONDS);

    }

}