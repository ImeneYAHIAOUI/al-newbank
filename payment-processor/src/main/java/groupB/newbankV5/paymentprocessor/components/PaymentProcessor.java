package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.config.KafkaProducerService;
import groupB.newbankV5.paymentprocessor.connectors.ExternalBankProxy;
import groupB.newbankV5.paymentprocessor.connectors.dto.AccountDto;
import groupB.newbankV5.paymentprocessor.connectors.dto.CreditCardDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.*;
import groupB.newbankV5.paymentprocessor.entities.*;
import groupB.newbankV5.paymentprocessor.interfaces.*;
import groupB.newbankV5.paymentprocessor.repositories.PaymentTokenRepository;
import groupB.newbankV5.paymentprocessor.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class PaymentProcessor implements ITransactionProcessor, IFundsHandler, IFraudDetector {
    private static final Logger log = Logger.getLogger(PaymentProcessor.class.getName());

    private static final double HIGH_TRANSACTION_THRESHOLD = 10000;

    private static final double LOW_TRANSACTION_THRESHOLD = 0;

    private static final String NEWBANK_IBAN_REGEX = "^FR\\d{2}20523\\d+$";

    private final ICostumerCare customerCare;

    private final ExternalBankProxy externalBankProxy;

    private final KafkaProducerService kafkaProducerService;
   private final TransactionRepository transactionRepository;
   private final PaymentTokenRepository paymentTokenRepository;



   @Autowired
    public PaymentProcessor(ICostumerCare costumerCare, ExternalBankProxy externalBankProxy, KafkaProducerService kafkaProducerService, TransactionRepository transactionRepository, PaymentTokenRepository paymentTokenRepository) {
        this.customerCare = costumerCare;
        this.externalBankProxy = externalBankProxy;
        this.kafkaProducerService = kafkaProducerService;
        this.transactionRepository = transactionRepository;
       this.paymentTokenRepository = paymentTokenRepository;
   }


    @Override
    public PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails) {
        log.info("Authorizing payment");
        Transaction transaction = new Transaction();
        String authToken = generateAuthToken();
        paymentTokenRepository.save(new PaymentToken(authToken));
        AccountDto accountDto = customerCare.getAccountByCreditCard(paymentDetails.getCardNumber(), paymentDetails.getExpirationDate(), paymentDetails.getCvv());
        if (accountDto == null) {
            return new PaymentResponseDto(false, "Account not found", authToken);
        }
        fillCommunTransactionInformation(transaction, accountDto, paymentDetails.getToAccountIBAN(), paymentDetails.getToAccountBIC(), paymentDetails.getAmount());
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

    private void fillCommunTransactionInformation(Transaction transaction, AccountDto accountDto, String toAccountIBAN, String toAccountBIC, double amount) {
        transaction.setId(UUID.randomUUID());
        BankAccount sender = new BankAccount(accountDto.getIBAN(), accountDto.getBIC());
        BankAccount receiver = new BankAccount(toAccountIBAN, toAccountBIC);
        transaction.setSender(sender);
        transaction.setRecipient(receiver);
        transaction.setAmount(String.valueOf(amount));
    }

    @Override
    public TransferResponseDto authorizeTransfer(TransferDto transferDetails) {
        log.info("\u001B[32mAuthorizing transfer\u001B[0m");
        Transaction transaction = new Transaction();
        AccountDto accountDto = customerCare.getAccountByIBAN(transferDetails.getFromAccountIBAN());
        String authToken = generateAuthToken();
        paymentTokenRepository.save(new PaymentToken(authToken));
        if (accountDto == null) {
            return new TransferResponseDto(false, "Account not found", authToken);
        }

        fillCommunTransactionInformation(transaction, accountDto, transferDetails.getToAccountIBAN(), transferDetails.getToAccountBIC(), transferDetails.getAmount());
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
        if(accountDto.getRestOfTheWeekLimit() < transferDetails.getAmount()){
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            kafkaProducerService.sendMessage(transaction);
            return new TransferResponseDto(false, "limit transfer", authToken);
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
        String authToken = generateAuthToken();
        paymentTokenRepository.save(new PaymentToken(authToken));
        if(accountDto == null){
            log.info("\u001B[31mCredit card does not existe\u001B[0m");
            return new CreditCardResponseDto(false, "Credit card does not existe", authToken);
        }
        List<CreditCardDto> creditCards = accountDto.getCreditCards();
        CreditCardDto creditCardDto = creditCards.stream().filter(creditCard -> creditCard.getCardNumber().equals(paymentDetails.getCardNumber())).findFirst().orElseThrow();
        if(creditCardDto.getRestOfLimit() < paymentDetails.getAmount()){
            log.info("\u001B[31mCredit card limit exceeded\u001B[0m");
            return new CreditCardResponseDto(false, "Credit card limit exceeded", authToken);
        }
        if(accountDto.getBalance() < paymentDetails.getAmount()){
            log.info("\u001B[31mInsufficient funds\u001B[0m");
            return new CreditCardResponseDto(false, "Insufficient funds", authToken);
        }
        log.info("\u001B[32mCredit card is valid\u001B[0m");
        return new CreditCardResponseDto(true, "Credit card is valid", authToken, accountDto.getIBAN(), accountDto.getBIC(), creditCardDto.getCardType());


    }

    @Override
    public String reserveFunds(Transaction transaction) {
        try {
            Optional<PaymentToken> paymentToken = paymentTokenRepository.findById(transaction.getAuthorizationToken());
            if (paymentToken.isEmpty()) {
                return "Invalid token";
            }
            if (paymentToken.get().isUsed()) {
                return "Token already used";
            }
            if (paymentToken.get().getExpiryDate().isBefore(LocalDateTime.now())) {
                return "Token expired";
            }
            paymentToken.get().setUsed(true);
            paymentTokenRepository.save(paymentToken.get());
            transaction.setStatus(TransactionStatus.PENDING_SETTLEMENT);
            transactionRepository.save(transaction);
            customerCare.reserveFunds(Double.parseDouble(transaction.getAmount()), transaction.getCreditCard().getCardNumber(), transaction.getCreditCard().getExpiryDate(), transaction.getCreditCard().getCvv());
            return "Funds reserved";
        }
        catch (Exception e){
            log.info("\u001B[30mError while reserving funds: \u001B[0m" + e.getMessage());
            return "Error while reserving funds: " + e.getMessage();
        }
    }


    @Override
    public boolean hasSufficientFunds(AccountDto accountDto, double amount) {
        double balance =  accountDto.getBalance();
        return balance > amount;
    }



    @Override
    public void deductFunds(long accountId, double amount) {

        customerCare.updateBalance(accountId, amount, "withdraw");
    }

    @Override
    public void depositFund(long accountId, double amount) {
        log.info("Depositing funds");
        customerCare.updateBalance(accountId, amount, "deposit");
    }

    @Override
    public boolean isFraudulent(PaymentDetailsDTO transaction) {
        return checkAmount(transaction.getAmount());
    }

    @Override
    public boolean isFraudulent(TransferDto transaction) {
        return checkAmount(transaction.getAmount());
    }



    private boolean checkAmount(double amount) {
        log.info("Checking for fraudulent transaction");

        if (amount > HIGH_TRANSACTION_THRESHOLD ) {
            log.info("The amount is high enough to be considered a fraud risk");
            return true;
        }

        if (amount  < 0) {
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
        StringBuilder sb = new StringBuilder(30);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 30; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }


}

