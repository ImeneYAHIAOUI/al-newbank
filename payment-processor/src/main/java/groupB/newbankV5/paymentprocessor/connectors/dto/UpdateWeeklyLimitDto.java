package groupB.newbankV5.paymentprocessor.connectors.dto;

import java.math.BigDecimal;

public class UpdateWeeklyLimitDto {

    private BigDecimal amount;

    public UpdateWeeklyLimitDto(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
