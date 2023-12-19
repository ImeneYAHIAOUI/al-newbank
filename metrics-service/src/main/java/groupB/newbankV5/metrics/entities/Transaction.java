package groupB.newbankV5.metrics.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {

    private String amount;
    private String fees;
    @JsonProperty("recipient_iban")
    private String recipientIban;
    @JsonProperty("sender_bic")
    private String senderBic;

    @JsonProperty("is_external")
    private boolean is_external;
    @JsonProperty("sender_iban")
    private String senderIban;
    @JsonProperty("application_id")
    private long applicationId;
    @JsonProperty("credit_card_type")
    private String creditCardType;

    @JsonProperty("recipient_bic")
    private String recipientBic;

    @JsonProperty("authorization_token")
    private String authorizationToken;
    private String id;
    private long time;
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
}
