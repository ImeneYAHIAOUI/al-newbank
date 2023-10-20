package groupB.newbankV5.externalbank.controllers.dto;

import java.math.BigDecimal;

public class IbanAmountDto {
    private String iban;
    private BigDecimal amount;

    public IbanAmountDto() {
    }

    public IbanAmountDto(String iban,BigDecimal amount) {
        this.iban = iban;
        this.amount = amount;
    }



    public BigDecimal getAmount() {
        return amount;
    }

    public String getIBAN() {
        return iban;
    }

    public void setIBAN(String IBAN) {
        this.iban = IBAN;
    }

    @Override
    public String toString() {
        return "IbanAmountDto{" + "iban='" + iban + '\'' + ", amount=" + amount + '}';
    }
}
