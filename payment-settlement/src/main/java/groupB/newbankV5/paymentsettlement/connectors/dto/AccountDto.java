package groupB.newbankV5.paymentsettlement.connectors.dto;

import java.math.BigDecimal;

public class AccountDto {
    long id;
    BigDecimal balance;

    public AccountDto() {
    }

    public AccountDto(long accountId, BigDecimal balance) {
        this.id = accountId;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountDto{" + "accountId=" + id + ", balance=" + balance + '}';
    }
}
