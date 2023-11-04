package groupB.newbankV5.customercare.controllers.dto;

import java.math.BigDecimal;

public class bankAccountDto {
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public bankAccountDto(BigDecimal balance) {
        this.balance = balance;
    }

}
