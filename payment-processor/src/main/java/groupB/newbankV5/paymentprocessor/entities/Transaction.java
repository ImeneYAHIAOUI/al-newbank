package groupB.newbankV5.paymentprocessor.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    private BankAccount recipient;
    private BankAccount sender;
    private Boolean isExternal;
    private String authorizationToken;
    private BigDecimal amount;
    private TransactionStatus status;


    public Transaction(  BankAccount recipient, BankAccount sender, Boolean isExternal, String authorizationToken, BigDecimal amount, TransactionStatus status) {
        this.recipient = recipient;
        this.sender = sender;
        this.isExternal = isExternal;
        this.authorizationToken = authorizationToken;
        this.amount = amount;
        this.status = status;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public BankAccount getRecipient() {
        return recipient;
    }

    public BankAccount getSender() {
        return sender;
    }

    public void setSender(BankAccount sender) {
        this.sender = sender;
    }

    public Boolean getExternal() {
        return isExternal;
    }

    public void setExternal(Boolean external) {
        isExternal = external;
    }

    public void setRecipient(BankAccount recipient) {
        this.recipient = recipient;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(recipient, that.recipient) && Objects.equals(authorizationToken, that.authorizationToken) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipient, authorizationToken, amount);
    }

    public Transaction() {
    }

    public Transaction(BankAccount recipient, String authorizationToken, BigDecimal amount) {
        this.recipient = recipient;
        this.authorizationToken = authorizationToken;
        this.amount = amount;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
