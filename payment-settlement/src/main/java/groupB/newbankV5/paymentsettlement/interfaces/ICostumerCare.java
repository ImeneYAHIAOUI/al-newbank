package groupB.newbankV5.paymentsettlement.interfaces;

import groupB.newbankV5.paymentsettlement.connectors.dto.AccountDto;

import java.math.BigDecimal;

public interface ICostumerCare {

    AccountDto getAccountByIBAN(String accountNumber);


    void updateBalance(long accountId, BigDecimal amount, String operation);

    void releaseFunds(long accountId, BigDecimal amount);
}
