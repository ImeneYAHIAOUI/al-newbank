package groupB.newbankV5.customercare.components;

import groupB.newbankV5.customercare.components.dto.AccountCreationDto;
import groupB.newbankV5.customercare.controllers.dto.ReleaseFundsDto;
import groupB.newbankV5.customercare.entities.*;
import groupB.newbankV5.customercare.exceptions.InsufficientFundsException;
import groupB.newbankV5.customercare.interfaces.*;
import groupB.newbankV5.customercare.repositories.AccountRepository;
import groupB.newbankV5.customercare.repositories.AccountSavingsRepository;
import groupB.newbankV5.customercare.repositories.CustomerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

@Component
@Transactional
public class CustomerCare implements AccountFinder, AccountRegistration, SavingsAccountHandler, FundsHandler,BusinessAccount {

    private final AccountRepository accountRepository;
    private final CustomerProfileRepository customerProfileRepository;

    private final AccountSavingsRepository savingsAccountRepository;


    private static final Logger log = Logger.getLogger(CustomerCare.class.getName());

    @Autowired
    public CustomerCare(AccountRepository accountRepository, CustomerProfileRepository customerProfileRepository, AccountSavingsRepository savingsAccountRepository) {
        this.accountRepository = accountRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.savingsAccountRepository = savingsAccountRepository;
    }

    @Override
    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> findByIban(String iban) {
        return accountRepository.findByIBAN(iban);
    }

    @Override
    public Optional<Account> findByCreditCard(String number, String expiryDate, String cvv) {
        return accountRepository.findByCreditCardsCardNumberAndCreditCardsExpiryDateAndCreditCardsCvv(number,expiryDate,cvv);
    }

    @Override
    public Optional<Account> findByType(AccountType accountType) {
        return accountRepository.findByType(accountType);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account createAccount(AccountCreationDto accountCreationDto) {

        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setFirstName(accountCreationDto.getFirstName());
        customerProfile.setLastName(accountCreationDto.getLastName());
        customerProfile.setEmail(accountCreationDto.getEmail());
        customerProfile.setPhoneNumber(accountCreationDto.getPhoneNumber());
        customerProfile.setBirthDate(accountCreationDto.getBirthDate());
        customerProfile.setFiscalCountry(accountCreationDto.getFiscalCountry());
        customerProfile.setAddress(accountCreationDto.getAddress());
        customerProfileRepository.save(customerProfile);
        Account account = new Account(customerProfile, generateRandomIBAN(), generateRandomBIC(),
                Constants.WEEKLY_PAYMENT_LIMIT);
        return accountRepository.save(account);
    }

    @Override
    public Account updateFunds(Account account, BigDecimal amount, String operation) {

        if (operation.equals("withdraw")) {
            account.setBalance(account.getBalance().subtract(amount));
        } else if (operation.equals("deposit")) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            throw new IllegalArgumentException("Operation not supported");
        }

        return account;

    }

    @Override
    public Account addReservedFunds(Account account, BigDecimal amount, String cardNumber, String expirationDate, String cvv) {
        account.setReservedBalance(account.getReservedBalance().add(amount));
        account.setBalance(account.getBalance().subtract(amount));
        CreditCard creditCard = account.getCreditCards().stream().filter(card -> card.getCardNumber().equals(cardNumber) && card.getExpiryDate().equals(expirationDate) && card.getCvv().equals(cvv)).findFirst().orElseThrow();
        creditCard.setRestOfLimit(creditCard.getRestOfLimit().subtract(amount));
        return accountRepository.save(account);
    }

    @Override
    public Account deduceFromWeeklyLimit(Account account, BigDecimal amount) {
        account.setWeekly_payment_limit(account.getWeekly_payment_limit().subtract(amount));
        return accountRepository.save(account);
    }


    @Override
    public Account releaseReservedFunds(ReleaseFundsDto amount) {
        if(amount.getIBAN() == null) {
            addFeesToBankAccount(amount.getFees());
            return accountRepository.findByType(AccountType.NEWBANK_VIRTUAL_ACCOUNT).orElseThrow();
        }

        Account account = accountRepository.findByIBAN(amount.getIBAN()).orElseThrow();
        account.setReservedBalance(account.getReservedBalance().subtract(amount.getAmount()));
        if (amount.getReceiverIban() != null) {
            Account recipient = accountRepository.findByIBAN(amount.getReceiverIban()).orElseThrow();
            recipient.setBalance(account.getBalance().add(amount.getAmount()).subtract(amount.getFees()));
        }
        addFeesToBankAccount(amount.getFees());
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    private void addFeesToBankAccount( BigDecimal amount) {
        accountRepository.findByType(AccountType.NEWBANK_VIRTUAL_ACCOUNT).ifPresent(account -> {
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);
        });
    }

    private String generateRandomIBAN() {
        Random random = new Random();

        // Generate the account number part of IBAN (a random 20-digit number)
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            accountNumber.append(random.nextInt(10));
        }

        String iban =  "FR" + String.format("%02d", random.nextInt(100)) + "20523" + accountNumber;

        int checksum = calculateIBANChecksum(iban);
        iban = iban.substring(0, 9) + String.format("%02d", checksum) + iban.substring(8);

        return iban;
    }

    private String generateRandomBIC() {
        Random random = new Random();
        StringBuilder bic = new StringBuilder("BIC");
        for (int i = 0; i < 8; i++) {
            bic.append((char) (random.nextInt(26) + 'A'));
        }
        return bic.toString();
    }

    public int  calculateIBANChecksum(String iban) {
        iban = iban.substring(4) + iban.substring(0, 4);
        StringBuilder numericIBAN = new StringBuilder();
        for (char c : iban.toCharArray()) {
            if (Character.isDigit(c)) {
                numericIBAN.append(c);
            } else {
                numericIBAN.append(Character.getNumericValue(c) + 10);
            }
        }
        BigDecimal numericValue = new BigDecimal(numericIBAN.toString());
        BigDecimal remainder = numericValue.remainder(BigDecimal.valueOf(97));
        return BigDecimal.valueOf(98).subtract(remainder).intValue();

    }



    @Override
    public Account moveToSavingsAccount(Account account, BigDecimal amount) {
        System.out.printf("account: %s%n", account);
        if(amount.compareTo(account.getBalance()) > 0){
            throw new InsufficientFundsException("Insufficient funds "+account.getBalance()+" to move to savings account");
        }

        SavingsAccount savingsAccount =  account.getSavingsAccount();
        BigDecimal balance = savingsAccount.getBalance();
        savingsAccount.setBalance(balance.add(amount));
        account.setBalance(account.getBalance().subtract(amount));
        account.setSavingsAccount(savingsAccount);
        return accountRepository.save(account);
    }

    @Override
    public Account upgradeToBusinessAccount(Account account) {
        account.setType(AccountType.BUSINESS);
        account.setWeekly_payment_limit(Constants.WEEKLY_PAYMENT_LIMIT_BUSINESS);
        return accountRepository.save(account);
    }
    @Scheduled(cron = "0 0 0 1 * *")
    @Override
    public void resetCreditCardLimit(){
        List<Account> accounts = accountRepository.findAll();
        for(Account account: accounts){
            account.getCreditCards().forEach(creditCard -> {
                creditCard.setRestOfLimit(account.getType() == AccountType.PERSONAL? Constants.DEFAULT_CARD_LIMIT: Constants.DEFAULT_CARD_LIMIT_BUSINESS);
            });
            accountRepository.save(account);
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    @Override
    public void resetWeeklyPaymentLimit(){
        List<Account> accounts = accountRepository.findAll();
        for(Account account: accounts){
            account.setWeekly_payment_limit(account.getType() == AccountType.PERSONAL? Constants.WEEKLY_PAYMENT_LIMIT: Constants.WEEKLY_PAYMENT_LIMIT_BUSINESS);
            accountRepository.save(account);
        }
    }


}