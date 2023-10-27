package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.connectors.dto.AccountDto;
import groupB.newbankV5.paymentprocessor.connectors.dto.CreditCardInformationDto;

import java.math.BigDecimal;

public interface ICostumerCare {

    AccountDto getAccountByIBAN(String accountNumber);

    AccountDto getAccountByCreditCard(String cardNumber, String expiryDate, String cvv);


    void updateBalance(long accountId, BigDecimal amount, String operation);

     void reserveFunds(long accountId, BigDecimal amount, String cardNumber, String expirationDate, String cvv);
}
