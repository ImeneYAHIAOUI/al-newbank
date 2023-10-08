package groupB.newbankV5.customercare.interfaces;

import groupB.newbankV5.customercare.entities.Account;

import java.util.List;
import java.util.Optional;

public interface AccountFinder {

    Optional<Account> findAccountById(Long id);
    List<Account> findAll();

}
