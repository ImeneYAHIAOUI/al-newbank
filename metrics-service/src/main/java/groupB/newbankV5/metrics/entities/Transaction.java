package groupB.newbankV5.metrics.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Objects;


@Document(collection = "ptransaction")
public class Transaction {


    @Field("amount")
    private String amount;
    @Field("fees")
    private String fees;
    @Field("recipient_iban")
    private String recipientIban;
    @Field("sender_bic")
    private String senderBic;

    @Field("is_external")
    private boolean is_external;
    @Field("sender_iban")
    private String senderIban;
    @Field("application_id")
    @Indexed
    private long applicationId;
    @Field("credit_card_type")
    private String creditCardType;

    @Field("recipient_bic")
    private String recipientBic;

    @Field("_id")  // Use the same name as in the JSON document
    private String mongoId;

    @Field("authorization_token")
    private String authorizationToken;
    @Field("id")
    private String id;

    @Field("time")
    private long time;

    @Field("status")
    private String status;

    public String getAmount() {
        return amount;
    }

    public String getFees() {
        return fees;
    }

    public String getRecipientIban() {
        return recipientIban;
    }

    public String getSenderBic() {
        return senderBic;
    }

    public boolean isIs_external() {
        return is_external;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public String getRecipientBic() {
        return recipientBic;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public void setRecipientIban(String recipientIban) {
        this.recipientIban = recipientIban;
    }

    public void setSenderBic(String senderBic) {
        this.senderBic = senderBic;
    }

    public void setIs_external(boolean is_external) {
        this.is_external = is_external;
    }

    public void setSenderIban(String senderIban) {
        this.senderIban = senderIban;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public void setRecipientBic(String recipientBic) {
        this.recipientBic = recipientBic;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIs_external() {
        return this.is_external;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount='" + amount + '\'' +
                ", fees='" + fees + '\'' +
                ", recipientIban='" + recipientIban + '\'' +
                ", senderBic='" + senderBic + '\'' +
                ", is_external=" + is_external +
                ", senderIban='" + senderIban + '\'' +
                ", applicationId=" + applicationId +
                ", creditCardType='" + creditCardType + '\'' +
                ", recipientBic='" + recipientBic + '\'' +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", id='" + id + '\'' +
                ", time=" + time +
                ", status='" + status + '\'' +
                '}';
    }
}
