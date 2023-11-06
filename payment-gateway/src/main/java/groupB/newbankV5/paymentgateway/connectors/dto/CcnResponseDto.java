package groupB.newbankV5.paymentgateway.connectors.dto;

import groupB.newbankV5.paymentgateway.entities.CardType;

public class CcnResponseDto {

    boolean response;
    String message;
    String authToken;

    String AccountIBAN;
    String AccountBIC;
    CardType cardType;
    String bankName;



    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public CcnResponseDto() {
    }

    public CcnResponseDto(boolean response) {
        this.response = response;
    }

    public CcnResponseDto(boolean response, String message) {
        this.response = response;
        this.message = message;
    }

    public CcnResponseDto(boolean response, String message, String authToken) {
        this.response = response;
        this.message = message;
        this.authToken = authToken;
    }

    public boolean isResponse() {
        return response;
    }

    public String getAccountIBAN() {
        return AccountIBAN;
    }

    public void setAccountIBAN(String accountIBAN) {
        AccountIBAN = accountIBAN;
    }

    public String getAccountBIC() {
        return AccountBIC;
    }

    public void setAccountBIC(String accountBIC) {
        AccountBIC = accountBIC;
    }

    public boolean isApproved() {
        return response;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean getResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
