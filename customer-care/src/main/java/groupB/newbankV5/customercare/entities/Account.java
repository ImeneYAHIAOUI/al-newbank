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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CustomerProfile_id")
    private CustomerProfile customerProfile;
    private String IBAN;
    private String BIC;
    private BigDecimal balance;

    private BigDecimal reservedBalance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="SavingsAccount_id")
    private SavingsAccount savingsAccount;

    @OneToMany(targetEntity = CreditCard.class, mappedBy = "account", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CreditCard> creditCards = new ArrayList<>();

    public CustomerProfile getCustomerProfile() {
        return customerProfile;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCard) {
        this.creditCards = creditCard;
    }
    public void addCreditCard(CreditCard creditCard) {
        this.creditCards.add(creditCard);
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
        this.reservedBalance = BigDecimal.valueOf(0);
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

        return "Account{" +
                "id=" + id +
                ", customerProfile=" + customerProfile +
                ", IBAN='" + IBAN + '\'' +
                ", BIC='" + BIC + '\'' +
                ", balance=" + balance +
                ", savingsAccount=" + savingsAccount +
                '}';
    }

    public void setSavingsAccount(SavingsAccount savingsAccount) {
        this.savingsAccount = savingsAccount;
    }

    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }

    public BigDecimal getReservedBalance() {
        return reservedBalance;
    }

    public void setReservedBalance(BigDecimal reservedBalance) {
        this.reservedBalance = reservedBalance;
    }
}
