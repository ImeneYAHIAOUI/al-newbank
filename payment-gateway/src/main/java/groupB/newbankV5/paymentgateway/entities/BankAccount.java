package groupB.newbankV5.paymentgateway.entities;

import javax.persistence.Embeddable;

@Embeddable
public class BankAccount {
    private String IBAN;
    private String BIC;

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
