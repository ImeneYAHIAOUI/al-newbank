package groupB.newbankV5.customercare.controllers.dto;

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
    public UpdateFundsDto(BigDecimal amount) {
        this.amount = amount;
    }



    public BigDecimal getAmount() {
        return amount;
    }
}
