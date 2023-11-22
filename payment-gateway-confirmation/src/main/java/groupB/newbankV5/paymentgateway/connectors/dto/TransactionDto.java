package groupB.newbankV5.paymentgateway.connectors.dto;

import groupB.newbankV5.paymentgateway.entities.BankAccount;
import groupB.newbankV5.paymentgateway.entities.CardType;
import groupB.newbankV5.paymentgateway.entities.CreditCard;

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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    private LocalDateTime time;
    private Boolean isExternal;
    private String authorizationToken;
    private BigDecimal amount;
    private BigDecimal fees;
    private String status;
    private CardType creditCardType;
    private CreditCard creditCard;
    private String bank;

    public TransactionDto() {
    }

    public TransactionDto(UUID id, BankAccount recipient, BankAccount sender, LocalDateTime time, Boolean isExternal, String authorizationToken, BigDecimal amount, BigDecimal fees, String status, CardType creditCardType, CreditCard creditCard, String bank) {
        this.id = id;
        this.recipient = recipient;
        this.sender = sender;
        this.time = time;
        this.isExternal = isExternal;
        this.authorizationToken = authorizationToken;
        this.amount = amount;
        this.fees = fees;
        this.status = status;
        this.creditCardType = creditCardType;
        this.creditCard = creditCard;
        this.bank = bank;
    }

}
