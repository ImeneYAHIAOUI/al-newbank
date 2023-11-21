package groupB.newbankV5.metrics.controllers.dto;


import java.io.Serializable;
import java.util.Objects;

public class BankAccount implements Serializable {
    private String IBAN;
    private String BIC;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(IBAN, that.IBAN) && Objects.equals(BIC, that.BIC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IBAN, BIC);
    }

    public BankAccount(String IBAN, String BIC) {
        this.IBAN = IBAN;
        this.BIC = BIC;
    }

    public BankAccount() {

    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }
}
