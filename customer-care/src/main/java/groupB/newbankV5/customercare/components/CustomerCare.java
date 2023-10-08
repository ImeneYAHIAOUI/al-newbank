package groupB.newbankV5.customercare.components;

import groupB.newbankV5.customercare.components.dto.AccountCreationDto;
import groupB.newbankV5.customercare.controllers.CostumerController;
import groupB.newbankV5.customercare.entities.Account;
import groupB.newbankV5.customercare.entities.CustomerProfile;
import groupB.newbankV5.customercare.interfaces.AccountFinder;
import groupB.newbankV5.customercare.interfaces.AccountRegistration;
import groupB.newbankV5.customercare.repositories.AccountRepository;
import groupB.newbankV5.customercare.repositories.CreditCardRepository;
import groupB.newbankV5.customercare.repositories.CustomerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CustomerCare implements AccountFinder, AccountRegistration {

    private final AccountRepository accountRepository;
    private final CustomerProfileRepository customerProfileRepository;

    private static final Logger log = Logger.getLogger(CustomerCare.class.getName());

    @Autowired
    public CustomerCare(AccountRepository accountRepository, CustomerProfileRepository customerProfileRepository) {
        this.accountRepository = accountRepository;
        this.customerProfileRepository = customerProfileRepository;
    }

    @Override
    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
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
        Account account = new Account(customerProfile, generateRandomIBAN(), generateRandomBIC());
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    private String generateRandomIBAN() {
        Random random = new Random();

        // Generate the account number part of IBAN (a random 20-digit number)
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            accountNumber.append(random.nextInt(10));
        }

        String iban =  "FR00" + accountNumber;

        int checksum = calculateIBANChecksum(iban);
        iban = iban.substring(0, 2) + String.format("%02d", checksum) + iban.substring(4);

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


}
