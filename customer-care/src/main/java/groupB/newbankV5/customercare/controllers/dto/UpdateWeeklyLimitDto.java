package groupB.newbankV5.customercare.controllers.dto;

import java.math.BigDecimal;

public class UpdateWeeklyLimitDto {

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
