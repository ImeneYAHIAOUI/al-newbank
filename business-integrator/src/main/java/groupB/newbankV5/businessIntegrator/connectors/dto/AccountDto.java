package groupB.newbankV5.businessIntegrator.connectors.dto;

import java.math.BigDecimal;

public class AccountDto {
    long accountId;
    BigDecimal balance;

    public AccountDto() {
    }

    public AccountDto(long accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
