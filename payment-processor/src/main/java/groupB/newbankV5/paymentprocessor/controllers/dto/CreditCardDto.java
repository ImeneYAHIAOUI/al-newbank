package groupB.newbankV5.paymentprocessor.controllers.dto;

import groupB.newbankV5.paymentprocessor.entities.CreditCard;

public class CreditCardDto {

    private String cardNumber;
    private String expiryDate;
    private String cvv;

    public CreditCardDto() {
    }

    public CreditCardDto(String cardNumber, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }


    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }


    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public static CreditCard toCreditCard(CreditCardDto creditCardDto) {
        return new CreditCard(creditCardDto.getCardNumber(), creditCardDto.getExpiryDate(), creditCardDto.getCvv());
    }
}
