package groupB.newbankV5.paymentgateway.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

//@Entity
public class Transaction {

//    @Id
//    @GeneratedValue
//    @Column(name = "Transaction_id", nullable = false)
    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "merchant_merchant_id")
    private Merchant merchant;
    private String authorizationToken;
    private BigDecimal amount;
    private BigDecimal fees;
    private TransactionStatus status;

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(merchant, that.merchant) && Objects.equals(authorizationToken, that.authorizationToken) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(merchant, authorizationToken, amount);
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
