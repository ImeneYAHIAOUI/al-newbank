package groupB.newbankV5.customercare.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class SavingsAccount {

    @Id
    @GeneratedValue
    private Long id;

    protected BigDecimal balance;
    private final double INTEREST_RATE = 0.02;


    public SavingsAccount() {
        this.setBalance(BigDecimal.ZERO);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "balance=" + balance +
                '}';
    }
}
