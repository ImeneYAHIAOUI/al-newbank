package groupB.newbankV5.paymentsettlement.connectors.dto;

import java.math.BigDecimal;

public class ReleaseFundsDto {
    private double amount;
    private String IBAN;
    private String receiverIban;
    private double fees;

    public ReleaseFundsDto(double amount, double fees, String IBAN, String receiverIban) {
        this.amount = amount;
        this.IBAN = IBAN;
        this.receiverIban = receiverIban;
        this.fees = fees;
    }

    public ReleaseFundsDto(double amount,double fees, String iban) {
        this.amount = amount;
        this.fees = fees;
        this.IBAN = iban;
    }

    public ReleaseFundsDto(double fees) {

        this.fees = fees;

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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


    public void setFees(double fees) {
        this.fees = fees;
    }
}
