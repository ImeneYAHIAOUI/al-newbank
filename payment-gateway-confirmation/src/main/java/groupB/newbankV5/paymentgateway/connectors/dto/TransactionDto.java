package groupB.newbankV5.paymentgateway.connectors.dto;

import groupB.newbankV5.paymentgateway.entities.BankAccount;
import groupB.newbankV5.paymentgateway.entities.CardType;
import groupB.newbankV5.paymentgateway.entities.CreditCard;
import groupB.newbankV5.paymentgateway.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionDto {

    UUID id;
    BankAccount recipient;
    BankAccount sender;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BankAccount getRecipient() {
        return recipient;
    }

    public void setRecipient(BankAccount recipient) {
        this.recipient = recipient;
    }

    public BankAccount getSender() {
        return sender;
    }

    public void setSender(BankAccount sender) {
        this.sender = sender;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Boolean getExternal() {
        return isExternal;
    }

    public void setExternal(Boolean external) {
        isExternal = external;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }



    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return String.valueOf(amount);
    }


    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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



    private long time;
    private Boolean isExternal;
    private String authorizationToken;
    private double amount;
    private String fees;
    private String status;
    private CardType creditCardType;
    private CreditCard creditCard;

    public TransactionDto() {
    }

    public static TransactionDto fromTransaction(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(Double.parseDouble(transaction.getAmount()));
        transactionDto.setAuthorizationToken(transaction.getAuthorizationToken());
        transactionDto.setCreditCard(transaction.getCreditCard());
        transactionDto.setCreditCardType(transaction.getCreditCardType());
        transactionDto.setExternal(transaction.getExternal());
        transactionDto.setFees(transaction.getFees());
        transactionDto.setId(transaction.getId());
        transactionDto.setRecipient(transaction.getRecipient());
        transactionDto.setSender(transaction.getSender());
        transactionDto.setStatus(transaction.getStatus().toString());
        transactionDto.setTime(transaction.getTime());
        return transactionDto;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "id=" + id +
                ", recipient=" + recipient +
                ", sender=" + sender +
                ", time=" + time +
                ", isExternal=" + isExternal +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", amount=" + amount  +
                ", fees='" + fees + '\'' +
                ", status='" + status + '\'' +
                ", creditCardType=" + creditCardType +
                ", creditCard=" + creditCard +
                '}';
    }


}
