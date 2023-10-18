package groupB.newbankV5.customercare.entities;

import io.micrometer.core.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "Account_id", nullable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name = "CustomerProfile_id")
    private CustomerProfile customerProfile;
    private String IBAN;
    private String BIC;
    private BigDecimal balance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="SavingsAccount_id")
    private SavingsAccount savingsAccount;

    @Embedded
    private CreditCard creditCard;

    public CustomerProfile getCustomerProfile() {
        return customerProfile;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public void setCustomerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
    }

    public Account() {
        this.savingsAccount = new SavingsAccount();
    }

    public Account(CustomerProfile customerProfile, String IBAN, String BIC ) {
        this.customerProfile = customerProfile;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.balance = BigDecimal.valueOf(0);
        this.savingsAccount = new SavingsAccount();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        String creditCard = "";
        if (this.creditCard != null) {
            creditCard = this.creditCard.toString();
        }
        return "Account{" +
                "id=" + id +
                ", customerProfile=" + customerProfile +
                ", IBAN='" + IBAN + '\'' +
                ", BIC='" + BIC + '\'' +
                ", balance=" + balance +
                ", creditCards=" + creditCard +
                ", savingsAccount=" + savingsAccount +
                '}';
    }

    public void setSavingsAccount(SavingsAccount savingsAccount) {
        this.savingsAccount = savingsAccount;
    }

    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }

}
