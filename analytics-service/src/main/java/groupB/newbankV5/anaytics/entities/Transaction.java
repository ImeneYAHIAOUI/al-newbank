package groupB.newbankV5.anaytics.entities;



import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Transaction {

    @Id
    private String id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "BIC", column = @Column(name = "recipient_BIC")),
            @AttributeOverride(
                    name = "IBAN", column = @Column(name = "recipient_IBAN"))
    })
    private BankAccount recipient;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "BIC", column = @Column(name = "sender_BIC")),
            @AttributeOverride(
                    name = "IBAN", column = @Column(name = "sender_IBAN"))
    })
    private BankAccount sender;
    private Boolean isExternal;

    private LocalDate time;

    private String authorizationToken;
    private BigDecimal amount;
    private BigDecimal fees;
    private TransactionStatus status;
    private CardType creditCardType;



    public TransactionStatus getStatus() {
        return status;
    }

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "BIC", column = @Column(name = "recipient_BIC")),
            @AttributeOverride(
                    name = "IBAN", column = @Column(name = "recipient_IBAN"))
    })
    public BankAccount getRecipient() {
        return recipient;
    }
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "BIC", column = @Column(name = "sender_BIC")),
            @AttributeOverride(
                    name = "IBAN", column = @Column(name = "sender_IBAN"))
    })
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", recipient=" + recipient +
                ", sender=" + sender +
                ", isExternal=" + isExternal +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", amount=" + amount +
                ", fees=" + fees +
                ", status=" + status +
                ", time=" + time +
                '}';
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

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public CardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CardType creditCardType) {
        this.creditCardType = creditCardType;
    }
}
