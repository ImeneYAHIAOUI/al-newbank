package groupB.newbankV5.paymentprocessor.entities;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@Table
public class Transaction  {


    public UUID getId() {
        return id;
    }


    public void setId(UUID id) {
        this.id = id;
    }

    @PrimaryKey
    private UUID id;

    private LocalDateTime time;
    private CardType creditCardType;

    private BankAccount recipient;

    private BankAccount sender;
    private Boolean isExternal;
    private String authorizationToken;
    private BigDecimal amount;
    private TransactionStatus status;
    private BigDecimal fees;
    private CreditCard creditCard;


    public Transaction(  BankAccount recipient, BankAccount sender, Boolean isExternal, String authorizationToken, BigDecimal amount, TransactionStatus status) {
        this.recipient = recipient;
        this.sender = sender;
        this.isExternal = isExternal;
        this.authorizationToken = authorizationToken;
        this.amount = amount;
        this.status = status;
        this.time = LocalDateTime.now();
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
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
        this.time = LocalDateTime.now();
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", recipient=" + recipient.toString() +
                ", sender=" + sender.toString() +
                ", isExternal=" + isExternal +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", amount=" + amount +
                ", fees=" + fees +
                ", status=" + status +
                ", time=" + time +
                '}';
    }


    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public CardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}