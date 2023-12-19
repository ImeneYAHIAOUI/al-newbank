package groupB.newbankV5.paymentsettlement.connectors.dto;

import java.math.BigDecimal;

public class AccountDto {
    long id;
    double balance;

    private double reservedBalance;

    public AccountDto() {
    }

    public AccountDto(long accountId, double balance, double reservedBalance) {
        this.id = accountId;
        this.balance = balance;
        this.reservedBalance = reservedBalance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "AccountDto{" + "accountId=" + id + ", balance=" + balance + '}';
    }


}
