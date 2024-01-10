package groupB.newbankV5.paymentgateway.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@RedisHash("Transaction")
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private BankAccount recipient;
    private BankAccount sender;
    private long time;
    private long applicationId;
    private Boolean isExternal;
    private String authorizationToken;
    private String amount;
    private String fees;
    private TransactionStatus status;
    private CardType creditCardType;
    private CreditCard creditCard;
    private String bank;
    private Long merchantId;
    public TransactionStatus getStatus() {
        return status;
    }
    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
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

    public Transaction(BankAccount recipient, String authorizationToken, String amount) {
        this.recipient = recipient;
        this.authorizationToken = authorizationToken;
        this.amount = amount;
        this.time = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setApplicationId(Long id) {
        this.applicationId = id;
    }

    public Long getApplicationId() {
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
                ", creditCardType=" + creditCardType +
                ", applicationId=" + applicationId +
                ", time=" + time +
                '}';
    }
}
