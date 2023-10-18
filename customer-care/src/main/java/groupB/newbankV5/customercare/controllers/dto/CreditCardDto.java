package groupB.newbankV5.customercare.controllers.dto;

import groupB.newbankV5.customercare.entities.Account;
import groupB.newbankV5.customercare.entities.CreditCard;


public class CreditCardDto {

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    public CreditCardDto( String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }



    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
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

    public static CreditCardDto creditCardFactory(CreditCard creditCard) {
        return new CreditCardDto(
                creditCard.getCardNumber(),
                creditCard.getCardHolderName(),
                creditCard.getExpiryDate(),
                creditCard.getCvv()
        );
    }
}
