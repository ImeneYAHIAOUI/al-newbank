package groupB.newbankV5.customercare.interfaces;

import groupB.newbankV5.customercare.entities.Account;

import java.math.BigDecimal;

public interface SavingsAccountHandler {

    Account moveToSavingsAccount(Account account, BigDecimal amount);
}
