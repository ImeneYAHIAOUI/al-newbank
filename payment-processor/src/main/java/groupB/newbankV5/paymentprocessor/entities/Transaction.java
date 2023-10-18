package groupB.newbankV5.paymentprocessor.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @Column(name = "Transaction_id", nullable = false)
    private Long id;
    private String fromAccount;
    private String toAccount;


    @Embedded
    private CreditCard creditCard;
    private BigDecimal amount;
    private TransactionType transactionType;

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public Transaction() {
    }

    public Transaction(String fromAccount, String toAccount, BigDecimal amount, TransactionType transactionType) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public Transaction(CreditCard creditCard, String toAccount, BigDecimal amount, TransactionType transactionType) {
        this.creditCard = creditCard;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionType = transactionType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
