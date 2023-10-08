package groupB.newbankV5.customercare.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "Account_id", nullable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name = "CustomerProfile_id")
    private CustomerProfile customerProfile;
    private String IBAN;
    private String BIC;
    private BigDecimal balance;

    @OneToMany(targetEntity = CreditCard.class, mappedBy = "account", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CreditCard> creditCards;

    public CustomerProfile getCustomerProfile() {
        return customerProfile;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public void setCustomerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
    }

    public Account() {
    }

    public Account(CustomerProfile customerProfile, String IBAN, String BIC ) {
        this.customerProfile = customerProfile;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.balance = BigDecimal.valueOf(0);
        this.creditCards = new ArrayList<>();
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

}
