package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.connectors.dto.AccountDto;


public interface ICostumerCare {

    AccountDto getAccountByIBAN(String accountNumber);

    AccountDto getAccountByCreditCard(String cardNumber, String expiryDate, String cvv);


    void updateBalance(long accountId, double amount, String operation);



    void reserveFunds(double amount, String cardNumber, String expirationDate, String cvv);

    void deduceWeeklyLimit(long accountId, double amount);

}
