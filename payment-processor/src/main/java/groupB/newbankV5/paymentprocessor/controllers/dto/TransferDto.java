package groupB.newbankV5.paymentprocessor.controllers.dto;

import java.math.BigDecimal;

public class TransferDto {
    private String fromAccountIBAN;
    private String fromAccountBIC;
    private String toAccountIBAN;
    private String toAccountBIC;
    private BigDecimal amount;

    public TransferDto() {
    }

    public TransferDto(String fromAccountIBAN, String fromAccountBIC, String toAccountIBAN, String toAccountBIC, BigDecimal amount) {
        this.fromAccountIBAN = fromAccountIBAN;
        this.fromAccountBIC = fromAccountBIC;
        this.toAccountIBAN = toAccountIBAN;
        this.toAccountBIC = toAccountBIC;
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

    public String getToAccountBIC() {
        return toAccountBIC;
    }

    public void setToAccountBIC(String toAccountBIC) {
        this.toAccountBIC = toAccountBIC;
    }

    public String getFromAccountBIC() {
        return fromAccountBIC;
    }

    public void setFromAccountBIC(String fromAccountBIC) {
        this.fromAccountBIC = fromAccountBIC;
    }
}
