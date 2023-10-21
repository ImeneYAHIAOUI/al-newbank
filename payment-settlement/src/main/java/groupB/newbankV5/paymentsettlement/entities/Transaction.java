package groupB.newbankV5.paymentsettlement.entities;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {

    private String id;
    private BankAccount recipient;
    private BankAccount sender;
    private Boolean isExternal;
    private String authorizationToken;
    private BigDecimal amount;
    private BigDecimal fees;
    private TransactionStatus status;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transaction{" + "id='" + id + '\'' + ", recipient=" + recipient + ", sender=" + sender + ", isExternal=" + isExternal + ", authorizationToken='" + authorizationToken + '\'' + ", amount=" + amount + ", fees=" + fees + ", status=" + status + '}';
    }
}
