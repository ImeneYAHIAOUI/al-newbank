package groupB.newbankV5.paymentprocessor.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @Column(name = "Transaction_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "merchant_merchant_id")
    private Merchant merchant;
    private String authorizationToken;
    private BigDecimal amount;

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Transaction() {
    }

    public Transaction(Merchant merchant, String authorizationToken, BigDecimal amount) {
        this.merchant = merchant;
        this.authorizationToken = authorizationToken;
        this.amount = amount;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
