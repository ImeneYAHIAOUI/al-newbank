package groupB.newbankV5.paymentsettlement.entities;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class Transaction implements Serializable {


    @JsonProperty("id")

    private String id;

    private BankAccount recipient;

    private BankAccount sender;
    private Boolean isExternal;
    private String authorizationToken;
    private String amount;


    @JsonProperty("fees")
    private String fees;
    private TransactionStatus status;
    private long time;

    private long applicationId;

    public CardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CardType creditCardType) {
        this.creditCardType = creditCardType;
    }

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



    public void setFees(String fees) {
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

    public String  getAmount() {
        return this.amount;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getApplicationId() {
        return applicationId;
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


    public String getFees() {
        return fees;
    }

}
