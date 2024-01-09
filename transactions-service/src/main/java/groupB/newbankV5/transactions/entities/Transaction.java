package groupB.newbankV5.transactions.entities;




import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    long applicationId;

    private long time;

    private String authorizationToken;
    private String amount;
    private String fees;
    private String status;
    private String creditCardType;



    public String getStatus() {
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
                ", creditCardType='" + creditCardType + '\'' +
                '}';
    }


    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getAmount() {
        return amount;
    }

    public String getFees() {
        return fees;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getSenderBic() {
        return sender.getBIC();
    }

    public String getSenderIban() {
        return sender.getIBAN();
    }

    public String getRecipientBic() {
        return recipient.getBIC();
    }

    public String getRecipientIban() {
        return recipient.getIBAN();
    }

    public Boolean getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(Boolean external) {
        isExternal = external;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSenderBic(String senderBic) {
        this.sender.setBIC(senderBic);
    }

    public void setSenderIban(String senderIban) {
        this.sender.setIBAN(senderIban);
    }

    public void setRecipientBic(String recipientBic) {
        this.recipient.setBIC(recipientBic);
    }

    public void setRecipientIban(String recipientIban) {
        this.recipient.setIBAN(recipientIban);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.toString();
    }
}
