package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.config.KafkaProducerService;
import groupB.newbankV5.paymentprocessor.connectors.ExternalBankProxy;
import groupB.newbankV5.paymentprocessor.connectors.dto.AccountDto;
import groupB.newbankV5.paymentprocessor.connectors.dto.CreditCardDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.*;
import groupB.newbankV5.paymentprocessor.entities.*;
import groupB.newbankV5.paymentprocessor.interfaces.*;
import groupB.newbankV5.paymentprocessor.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class PaymentProcessor implements ITransactionProcessor, IFundsHandler, IFraudDetector {
    private static final Logger log = Logger.getLogger(PaymentProcessor.class.getName());

    private static final BigDecimal HIGH_TRANSACTION_THRESHOLD = new BigDecimal(1000);

    private static final BigDecimal LOW_TRANSACTION_THRESHOLD = new BigDecimal(0);

    private static final String NEWBANK_IBAN_REGEX = "^FR\\d{2}20523\\d+$";

    private final ICostumerCare customerCare;

    private final ExternalBankProxy externalBankProxy;

    private final KafkaProducerService kafkaProducerService;
   private final TransactionRepository transactionRepository;



   @Autowired
    public PaymentProcessor(ICostumerCare costumerCare, ExternalBankProxy externalBankProxy, KafkaProducerService kafkaProducerService, TransactionRepository transactionRepository) {
        this.customerCare = costumerCare;
        this.externalBankProxy = externalBankProxy;
        this.kafkaProducerService = kafkaProducerService;
        this.transactionRepository = transactionRepository;
   }


    @Override
    public PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails) {
        log.info("Authorizing payment");
        Transaction transaction = new Transaction();
        AccountDto accountDto = customerCare.getAccountByCreditCard(paymentDetails.getCardNumber(), paymentDetails.getExpirationDate(), paymentDetails.getCvv());
        if (accountDto == null) {
            return new PaymentResponseDto(false, "Account not found", generateAuthToken());
        }
        fillCommunTransactionInformation(transaction, accountDto, paymentDetails.getToAccountIBAN(), paymentDetails.getToAccountBIC(), paymentDetails.getAmount());
        String authToken = generateAuthToken();
        transaction.setExternal(true);
        transaction.setAuthorizationToken(authToken);


        if(isFraudulent(paymentDetails)) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            kafkaProducerService.sendMessage(transaction);
            return new PaymentResponseDto(false, "Fraudulent transaction", authToken);
        }

        if (!hasSufficientFunds(accountDto, paymentDetails.getAmount())) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            kafkaProducerService.sendMessage(transaction);
            return new PaymentResponseDto(false, "Insufficient funds", authToken);
        }
        transactionRepository.save(transaction);
        transaction.setStatus(TransactionStatus.AUTHORIZED);

        customerCare.reserveFunds(paymentDetails.getAmount(), paymentDetails.getCardNumber(), paymentDetails.getExpirationDate(), paymentDetails.getCvv());
        kafkaProducerService.sendMessage(transaction);
        return new PaymentResponseDto(true, "Payment authorized", authToken);

    }

    private void fillCommunTransactionInformation(Transaction transaction, AccountDto accountDto, String toAccountIBAN, String toAccountBIC, BigDecimal amount) {
        transaction.setId(UUID.randomUUID());
        BankAccount sender = new BankAccount(accountDto.getIBAN(), accountDto.getBIC());
        BankAccount receiver = new BankAccount(toAccountIBAN, toAccountBIC);
        transaction.setSender(sender);
        transaction.setRecipient(receiver);
        transaction.setAmount(amount);
    }

    @Override
    public TransferResponseDto authorizeTransfer(TransferDto transferDetails) {
        log.info("Authorizing transfer");
        Transaction transaction = new Transaction();
        AccountDto accountDto = customerCare.getAccountByIBAN(transferDetails.getFromAccountIBAN());
        if (accountDto == null) {
            return new TransferResponseDto(false, "Account not found", generateAuthToken());
        }

        fillCommunTransactionInformation(transaction, accountDto, transferDetails.getToAccountIBAN(), transferDetails.getToAccountBIC(), transferDetails.getAmount());
        String authToken = generateAuthToken();
        transaction.setAuthorizationToken(authToken);

        if(isFraudulent(transferDetails)) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            kafkaProducerService.sendMessage(transaction);
            return new TransferResponseDto(false, "Fraudulent transaction", authToken);
        }
        if (!hasSufficientFunds(accountDto, transferDetails.getAmount())) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            kafkaProducerService.sendMessage(transaction);
            return new TransferResponseDto(false, "Insufficient funds", authToken);
        }
        if(accountDto.getRestOfTheWeekLimit().compareTo(transferDetails.getAmount()) < 0){
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            kafkaProducerService.sendMessage(transaction);
            return new TransferResponseDto(false, "limit transfer", generateAuthToken());
        }
        transaction.setStatus(TransactionStatus.AUTHORIZED);
        if (isNewBankAccount(transferDetails.getToAccountIBAN())) {
            transaction.setExternal(false);

            deductFunds(accountDto.getId(), transferDetails.getAmount());
            AccountDto destinationAccount = customerCare.getAccountByIBAN(transferDetails.getToAccountIBAN());
            depositFund(destinationAccount.getId(), transferDetails.getAmount());
            customerCare.deduceWeeklyLimit(accountDto.getId(), transferDetails.getAmount());
            transactionRepository.save(transaction);
            return new TransferResponseDto(true, "Transfer authorized", authToken);

        } else {
            if(externalBankProxy.authorizeTransfer(transferDetails).isAuthorized()){
                transaction.setExternal(true);
                kafkaProducerService.sendMessage(transaction);
                customerCare.deduceWeeklyLimit(accountDto.getId(), transferDetails.getAmount());
                return new TransferResponseDto(true, "Transfer authorized", authToken);
            }  else{
                transaction.setStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                kafkaProducerService.sendMessage(transaction);
                return new TransferResponseDto(false, "Transfer failed", authToken);

            }
        }

    }

    @Override
    public CreditCardResponseDto validateCreditCard(CreditCardInformationDto paymentDetails) {
        AccountDto accountDto = customerCare.getAccountByCreditCard(paymentDetails.getCardNumber(), paymentDetails.getExpirationDate(), paymentDetails.getCvv());
        if(accountDto == null){
            return new CreditCardResponseDto(false, "Credit card does not existe", generateAuthToken());
        }
        List<CreditCardDto> creditCards = accountDto.getCreditCards();
        CreditCardDto creditCardDto = creditCards.stream().filter(creditCard -> creditCard.getCardNumber().equals(paymentDetails.getCardNumber())).findFirst().orElseThrow();
        if(creditCardDto.getRestOfLimit().compareTo(paymentDetails.getAmount()) < 0){
            return new CreditCardResponseDto(false, "Credit card limit exceeded", generateAuthToken());
        }
        if(accountDto.getBalance().compareTo(paymentDetails.getAmount()) < 0){
            return new CreditCardResponseDto(false, "Insufficient funds", generateAuthToken());
        }
        return new CreditCardResponseDto(true, "Credit card is valid", generateAuthToken(), accountDto.getIBAN(), accountDto.getBIC(), creditCardDto.getCardType());


    }

    @Override
    public void reserveFunds(BigDecimal amount, String cardNumber, String expirationDate, String cvv) {
        try {
            customerCare.reserveFunds(amount, cardNumber, expirationDate, cvv);
        }
        catch (Exception e){
            log.info("Error while reserving funds: " + e.getMessage());
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
        customerCare.updateBalance(accountId, amount, "withdraw");
    }

    @Override
    public void depositFund(long accountId, BigDecimal amount) {
        log.info("Depositing funds");
        customerCare.updateBalance(accountId, amount, "deposit");
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

    private String generateAuthToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(10);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }


}

