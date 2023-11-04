package groupB.newbankV5.customercare.config;

import groupB.newbankV5.customercare.entities.Account;
import groupB.newbankV5.customercare.entities.AccountType;
import groupB.newbankV5.customercare.repositories.AccountRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Component
public class DatabaseInitializer {
    private final AccountRepository accountRepository;

    public DatabaseInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void initializeDatabase() {
        if (accountRepository.findByType(AccountType.NEWBANK_VIRTUAL_ACCOUNT).isEmpty()) {
            Account account = new Account();
            account.setType(AccountType.NEWBANK_VIRTUAL_ACCOUNT);
            account.setBalance(BigDecimal.ZERO);
            accountRepository.save(account);
        }

    }
}