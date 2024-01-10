package groupB.newbankV5.customercare.controllers.dto;

import java.math.BigDecimal;

public class ReleaseFundsDto implements java.io.Serializable {
    private BigDecimal amount;
    private String IBAN;
    private String receiverIban;
    private BigDecimal fees;

    public ReleaseFundsDto() {

    }

    public ReleaseFundsDto(BigDecimal amount, BigDecimal fees, String IBAN, String receiverIban) {
        this.amount = amount;
        this.IBAN = IBAN;
        this.receiverIban = receiverIban;
        this.fees = fees;
    }

    public ReleaseFundsDto(BigDecimal amount, String iban) {
        this.amount = amount;
        IBAN = iban;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getReceiverIban() {
        return receiverIban;
    }

    public void setReceiverIban(String receiverIban) {
        this.receiverIban = receiverIban;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }
}
