package groupB.newbankV5.paymentprocessor.controllers.dto;



import com.fasterxml.jackson.annotation.JsonProperty;
import groupB.newbankV5.paymentprocessor.entities.*;

import java.util.UUID;

public class TransactionDto {

    String id;
    BankAccountDto recipient;
    BankAccountDto sender;
    private long time;
    private Boolean isExternal;
    private String authorizationToken;
    private double amount;
    private String fees;
    private String status;
    private String creditCardType;
    private CreditCardDto creditCard;

    public TransactionDto() {
    }

    public double getAmount() {
        return amount;
    }

    public String getFees() {
        return fees;
    }

    public String getRecipientIban() {
        return recipient.getIBAN();
    }

    public String getSenderBic() {
        return sender.getBIC();
    }


    public String getId() {
        return id;
    }

    public String getSenderIban() {
        return sender.getIBAN();
    }

    public long getApplicationId() {
        return time;
    }







    public BankAccountDto getRecipient() {
        return recipient;
    }

    public void setRecipient(BankAccountDto recipient) {
        this.recipient = recipient;
    }

    public BankAccountDto getSender() {
        return sender;
    }

    public void setSender(BankAccountDto sender) {
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



    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public CreditCardDto getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardDto creditCard) {
        this.creditCard = creditCard;
    }






    public static Transaction fromTransactionDto(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.fromString(transactionDto.getId()));
        transaction.setAmount(Double.toString(transactionDto.getAmount()));
        transaction.setAuthorizationToken(transactionDto.getAuthorizationToken());
        transaction.setCreditCard(CreditCardDto.toCreditCard(transactionDto.getCreditCard()));
        transaction.setCreditCardType(CardType.fromString(transactionDto.getCreditCardType()));
        transaction.setExternal(transactionDto.getExternal());
        transaction.setRecipient(BankAccountDto.toBankAccount(transactionDto.getRecipient()));
        transaction.setSender(BankAccountDto.toBankAccount(transactionDto.getSender()));
        transaction.setStatus(TransactionStatus.fromString(transactionDto.getStatus()));
        transaction.setTime(transactionDto.getTime());
        return transaction;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "id='" + id + '\'' +
                ", recipient=" + recipient +
                ", sender=" + sender +
                ", time=" + time +
                ", isExternal=" + isExternal +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", amount='" + amount + '\'' +
                ", fees='" + fees + '\'' +
                ", status='" + status + '\'' +
                ", creditCardType='" + creditCardType + '\'' +
                ", creditCard=" + creditCard +
                '}';
    }


}
