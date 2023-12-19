package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.connectors.dto.AccountDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferDto;


public interface IFundsHandler {

    boolean hasSufficientFunds(AccountDto accountDto, double amount);


    void deductFunds(long accountId, double amount);

    void depositFund(long accountId, double amount);

    boolean isFraudulent(TransferDto transaction);
}
