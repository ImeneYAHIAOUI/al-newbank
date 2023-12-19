package groupB.newbankV5.paymentsettlement.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    private String id;
    private BankAccount recipient;
    private BankAccount sender;
    private Boolean isExternal;
    private String authorizationToken;
    private String amount;
    private String fees;

    private long time;

    private long applicationId;
    private TransactionStatus status;
    private CardType creditCardType;

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

    public String getFees() {
        return fees;
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


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public String getAmount() {
        return amount;
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

    public CardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }
}
