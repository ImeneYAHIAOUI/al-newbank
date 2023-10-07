package groupB.newbankV5.paymentprocessor.controllers.dto;

import java.math.BigDecimal;

public class TransferDto {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;

    public TransferDto() {
    }

    public TransferDto(String fromAccount, String toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }


    @Override
    public String toString() {
        return "InternalTransferDto{" +
                "fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", amount=" + amount +
                '}';
    }
}
