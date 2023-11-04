package groupB.newbankV5.customercare.interfaces;

import groupB.newbankV5.customercare.controllers.dto.CreditCardDto;
import groupB.newbankV5.customercare.entities.Account;
import groupB.newbankV5.customercare.entities.AccountType;

import java.util.List;
import java.util.Optional;

public interface AccountFinder {

    Optional<Account> findAccountById(Long id);
    Optional<Account> findByIban(String Iban);
    Optional<Account> findByCreditCard(String number, String expiryDate, String cvv);

    Optional<Account> findByType(AccountType accountType);

    List<Account> findAll();

}
