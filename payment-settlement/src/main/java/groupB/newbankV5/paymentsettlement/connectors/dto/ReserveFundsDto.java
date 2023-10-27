package groupB.newbankV5.paymentsettlement.connectors.dto;

import java.math.BigDecimal;

public class ReserveFundsDto {
    BigDecimal amount;

    public ReserveFundsDto(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
