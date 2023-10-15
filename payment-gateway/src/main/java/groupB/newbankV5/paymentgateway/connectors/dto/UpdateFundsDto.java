package groupB.newbankV5.paymentgateway.connectors.dto;

import java.math.BigDecimal;

public class UpdateFundsDto {

    private BigDecimal amount;

    private String operation;

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public UpdateFundsDto() {}

    public UpdateFundsDto(BigDecimal amount, String operation) {
        this.amount = amount;
        this.operation = operation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
