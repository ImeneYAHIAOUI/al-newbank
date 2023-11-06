package groupB.newbankV5.businessIntegrator.entities;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Objects;
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="id" )
public class Merchant {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String email;
    @Embedded
    private BankAccount bankAccount;
    @OneToOne
    @JsonBackReference
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Merchant merchant = (Merchant) o;
        return Objects.equals(name, merchant.name) && Objects.equals(email, merchant.email) && Objects.equals(bankAccount, merchant.bankAccount) && Objects.equals(application, merchant.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, bankAccount);
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + id +
                ", merchantName='" + name + '\'' +
                ", merchantEmail='" + email + '\'' +
                ", bankAccount=" + bankAccount +
                '}';
    }
}
