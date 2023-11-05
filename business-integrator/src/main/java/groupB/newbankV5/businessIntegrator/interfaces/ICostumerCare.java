package groupB.newbankV5.businessIntegrator.interfaces;

import groupB.newbankV5.businessIntegrator.connectors.dto.AccountDto;

import java.math.BigDecimal;

public interface ICostumerCare {

    AccountDto getAccountByIBAN(String accountNumber);

    AccountDto getAccountByCreditCard(String cardNumber, String expiryDate, String cvv);


    void updateBalance(long accountId, BigDecimal amount, String operation);
}
