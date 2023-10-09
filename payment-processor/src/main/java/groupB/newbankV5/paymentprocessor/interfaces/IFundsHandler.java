package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.connectors.dto.AccountDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferDto;

import java.math.BigDecimal;

public interface IFundsHandler {

    boolean hasSufficientFunds(AccountDto accountDto, BigDecimal amount);


    void deductFunds(long accountId, BigDecimal amount);

    void depositFund(long accountId, BigDecimal amount);

    boolean isFraudulent(TransferDto transaction);
}
