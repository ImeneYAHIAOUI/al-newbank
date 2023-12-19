package groupB.newbankV5.paymentsettlement.connectors.dto;

import java.math.BigDecimal;

public class IbanAmountDto {
    private String iban;
    private double amount;

    public IbanAmountDto() {
    }

    public IbanAmountDto(String iban,double amount) {
        this.iban = iban;
        this.amount = amount;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "IbanAmountDto{" + "iban='" + iban + '\'' + ", amount=" + amount + '}';
    }
}
