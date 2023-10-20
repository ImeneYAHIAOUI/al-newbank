package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.connectors.ExternalBankProxy;
import groupB.newbankV5.paymentprocessor.connectors.dto.AccountDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentResponseDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferResponseDto;
import groupB.newbankV5.paymentprocessor.entities.CreditCard;
import groupB.newbankV5.paymentprocessor.entities.Transaction;
import groupB.newbankV5.paymentprocessor.entities.TransactionType;
import groupB.newbankV5.paymentprocessor.interfaces.*;
import groupB.newbankV5.paymentprocessor.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.logging.Logger;

@Component
public class PaymentAuthorizer implements ITransactionProcessor, IFundsHandler, IFraudDetector {
    private static final Logger log = Logger.getLogger(PaymentAuthorizer.class.getName());
    private static final BigDecimal HIGH_TRANSACTION_THRESHOLD = new BigDecimal(1000);

    private static final BigDecimal LOW_TRANSACTION_THRESHOLD = new BigDecimal(0);

    private static final String NEWBANK_IBAN_REGEX = "^FR\\d{2}20523\\d+$";

    private final TransactionRepository transactionRepository;
    private final ICostumerCare costumerCare;


    private final ExternalBankProxy externalBankProxy;
    @Autowired
    public PaymentAuthorizer(TransactionRepository transactionRepository, ICostumerCare costumerCare, ICreditCardNetwork creditCardNetwork, ExternalBankProxy externalBankProxy) {
        this.transactionRepository = transactionRepository;
        this.costumerCare = costumerCare;
        this.externalBankProxy = externalBankProxy;
    }


    @Override
    public PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails) {
        log.info("Authorizing payment");

        if(isFraudulent(paymentDetails) ) {
            return new PaymentResponseDto(false, "Fraudulent transaction", generateAuthToken(10));
        }
        AccountDto accountDto = costumerCare.getAccountByCreditCard(paymentDetails.getCardNumber(), paymentDetails.getExpirationDate(), paymentDetails.getCvv());
        if (!hasSufficientFunds(accountDto, paymentDetails.getAmount())) {
            return new PaymentResponseDto(false, "Insufficient funds", generateAuthToken(10));
        }
        CreditCard creditCard = new CreditCard(paymentDetails.getCardHolderName(), paymentDetails.getCardNumber(), paymentDetails.getExpirationDate(), paymentDetails.getCvv());
        deductFunds(accountDto.getAccountId(), paymentDetails.getAmount());
        Transaction transaction = new Transaction(creditCard, paymentDetails.getToAccountIBAN(), paymentDetails.getAmount(), TransactionType.CREDIT);
        transactionRepository.save(transaction);
        return new PaymentResponseDto(true, "Payment authorized", generateAuthToken(10));

    }

    @Override
    public TransferResponseDto authorizeTransfer(TransferDto transferDetails) {
        log.info("Authorizing transfer");
        if(isFraudulent(transferDetails)) {
            return new TransferResponseDto(false, "Fraudulent transaction", generateAuthToken(10));
        }
        AccountDto accountDto = costumerCare.getAccountByIBAN(transferDetails.getFromAccountIBAN());
        if (!hasSufficientFunds(accountDto, transferDetails.getAmount())) {
            return new TransferResponseDto(false, "Insufficient funds", generateAuthToken(10));
        }
        Transaction transaction;
        if (isNewBankAccount(transferDetails.getToAccountIBAN())) {
            transaction = new Transaction(transferDetails.getFromAccountIBAN(), transferDetails.getToAccountIBAN(), transferDetails.getAmount(), TransactionType.TRANSFER);
            AccountDto accountDto2 = costumerCare.getAccountByIBAN(transferDetails.getToAccountIBAN());
            depositFund(accountDto2.getAccountId(), transferDetails.getAmount());
            transactionRepository.save(transaction);
            return new TransferResponseDto(true, "Transfer authorized", generateAuthToken(10));

        } else {
            //call external bank authorizer
            if(externalBankProxy.authorizeTransfer(transferDetails).isAuthorized()){
                transaction = new Transaction(transferDetails.getFromAccountIBAN(), transferDetails.getToAccountIBAN(), transferDetails.getAmount(), TransactionType.DEBIT);
                transactionRepository.save(transaction);
                return new TransferResponseDto(true, "Transfer authorized", generateAuthToken(10));
            }  else{
                return new TransferResponseDto(false, "Transfer failed", generateAuthToken(10));

            }
        }

    }

    @Override
    public boolean hasSufficientFunds(AccountDto accountDto, BigDecimal amount) {
        BigDecimal balance =  accountDto.getBalance();
        return balance.compareTo(amount) >= 0;
    }


    @Override
    public void deductFunds(long accountId, BigDecimal amount) {
        log.info("Deducting funds");
        costumerCare.updateBalance(accountId, amount, "withdraw");
    }

    @Override
    public void depositFund(long accountId, BigDecimal amount) {
        log.info("Depositing funds");
        costumerCare.updateBalance(accountId, amount, "deposit");
    }

    @Override
    public boolean isFraudulent(PaymentDetailsDTO transaction) {
        System.out.println(transaction);
        return checkAmount(transaction.getAmount());
    }

    @Override
    public boolean isFraudulent(TransferDto transaction) {
        return checkAmount(transaction.getAmount());
    }

    private boolean checkAmount(BigDecimal amount) {
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

    private boolean isNewBankAccount(String accountIban) {
        return accountIban.matches(NEWBANK_IBAN_REGEX);
    }

    private String generateAuthToken(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

}

