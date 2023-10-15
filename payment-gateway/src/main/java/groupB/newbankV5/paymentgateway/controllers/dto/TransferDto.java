package groupB.newbankV5.paymentgateway.controllers.dto;

import java.math.BigDecimal;

public class TransferDto {
    private String fromAccountIBAN;
    private String toAccountIBAN;
    private BigDecimal amount;

    public TransferDto() {
    }

    public TransferDto(String fromAccountIBAN, String toAccountIBAN, BigDecimal amount) {
        this.fromAccountIBAN = fromAccountIBAN;
        this.toAccountIBAN = toAccountIBAN;
        this.amount = amount;
    }

    public String getFromAccountIBAN() {
        return fromAccountIBAN;
    }

    public String getToAccountIBAN() {
        return toAccountIBAN;
    }

    public BigDecimal getAmount() {
        return amount;
    }


    @Override
    public String toString() {
        return "InternalTransferDto{" +
                "fromAccount='" + fromAccountIBAN + '\'' +
                ", toAccount='" + toAccountIBAN + '\'' +
                ", amount=" + amount +
                '}';
    }
}
