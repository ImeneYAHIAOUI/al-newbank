package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentResponseDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferResponseDto;
import groupB.newbankV5.paymentprocessor.entities.CreditCard;
import groupB.newbankV5.paymentprocessor.entities.Transaction;
import groupB.newbankV5.paymentprocessor.entities.TransactionType;
import groupB.newbankV5.paymentprocessor.interfaces.IFraudDetector;
import groupB.newbankV5.paymentprocessor.interfaces.IFundsHandler;
import groupB.newbankV5.paymentprocessor.interfaces.ITransactionProcessor;
import groupB.newbankV5.paymentprocessor.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;
import java.util.logging.Logger;

@Component
public class PaymentAuthorizer implements ITransactionProcessor, IFundsHandler, IFraudDetector {
    private static final Logger log = Logger.getLogger(PaymentAuthorizer.class.getName());
    private static final BigDecimal HIGH_TRANSACTION_THRESHOLD = new BigDecimal(1000);

    private static final BigDecimal LOW_TRANSACTION_THRESHOLD = new BigDecimal(0);

    private final TransactionRepository transactionRepository;
    private final ETFService etfService;
    @Autowired
    public PaymentAuthorizer(TransactionRepository transactionRepository, ETFService etfService) {
        this.transactionRepository = transactionRepository;
        this.etfService = etfService;
    }


    @Override
    public PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails) {
        log.info("Authorizing payment");
        if(isFraudulent(paymentDetails) ) {
            return new PaymentResponseDto(false, "Fraudulent transaction");
        }
        if (!hasSufficientFunds(paymentDetails)) {
            return new PaymentResponseDto(false, "Insufficient funds");
        }
        CreditCard creditCard = new CreditCard(paymentDetails.getCardHolderName(), paymentDetails.getCardNumber(), paymentDetails.getExpirationDate(), paymentDetails.getCvv());
        Transaction transaction = new Transaction(creditCard, paymentDetails.getToAccount(), paymentDetails.getAmount(), TransactionType.CREDIT);
        transactionRepository.save(transaction);
        return new PaymentResponseDto(true, "Payment authorized");

    }

    @Override
    public TransferResponseDto authorizeTransfer(TransferDto transferDetails) {
        log.info("Authorizing transfer");
        if(isFraudulent(transferDetails)) {
            return new TransferResponseDto(false, "Fraudulent transaction");
        }
        if (!hasSufficientFunds(transferDetails.getFromAccount(), transferDetails.getAmount())) {
            return new TransferResponseDto(false, "Insufficient funds");
        }
        Transaction transaction;
        if (isNewBankAccount(transferDetails.getToAccount())) {
            transaction = new Transaction(transferDetails.getFromAccount(), transferDetails.getToAccount(), transferDetails.getAmount(), TransactionType.TRANSFER);
        } else  {
            transaction = new Transaction(transferDetails.getFromAccount(), transferDetails.getToAccount(), transferDetails.getAmount(), TransactionType.DEBIT);
        }
        transactionRepository.save(transaction);
        if( ! etfService.validateTransaction(transferDetails.getToAccount(), transferDetails.getAmount().doubleValue())) {
            transactionRepository.delete(transaction);
            return new TransferResponseDto(false, "Transfer failed");
        }

        return new TransferResponseDto(true, "Transfer authorized");
    }

    @Override
    public boolean hasSufficientFunds(String accountNumber, BigDecimal amount) {
        return true;
    }

    @Override
    public boolean hasSufficientFunds(PaymentDetailsDTO paymentDetails) {
        return true;
    }

    @Override
    public void deductFunds(double amount) {
    }

    @Override
    public boolean isFraudulent(PaymentDetailsDTO transaction) {
        return checkAmound(transaction.getAmount());
    }

    @Override
    public boolean isFraudulent(TransferDto transaction) {
        return checkAmound(transaction.getAmount());
    }

    private boolean checkAmound(BigDecimal amount) {
        log.info("Checking for fraudulent transaction");

        if (amount.compareTo(HIGH_TRANSACTION_THRESHOLD) > 0) {
            log.info("The amount is high enough to be considered a fraud risk");
            return true;
        }

        if (amount.compareTo(LOW_TRANSACTION_THRESHOLD) < 0) {
            log.info("The amount is low enough to be considered a fraud risk");
            return true;
        }

        return false;
    }

    private boolean isNewBankAccount(String accountNumber) {
        Random random = new Random();
        return random.nextBoolean();
    }
}

