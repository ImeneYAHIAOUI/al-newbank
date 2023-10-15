package groupB.newbankV5.paymentgateway.entities;

import javax.persistence.*;

@Entity
public class Merchant {
    @Id
    @GeneratedValue
    @Column(name = "Merchant_id", nullable = false)
    private Long id;
    private String name;
    private String email;
    @Embedded
    private BankAccount bankAccount;
    @OneToOne(cascade = CascadeType.ALL)
    private Application application;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Merchant() {
    }

    public Merchant(String name, String email, BankAccount bankAccount) {
        this.name = name;
        this.email = email;
        this.bankAccount = bankAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String merchantName) {
        this.name = merchantName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String merchantEmail) {
        this.email = merchantEmail;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
