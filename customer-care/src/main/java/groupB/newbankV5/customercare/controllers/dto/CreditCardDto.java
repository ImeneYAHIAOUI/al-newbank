package groupB.newbankV5.customercare.controllers.dto;

import groupB.newbankV5.customercare.entities.CardType;
import groupB.newbankV5.customercare.entities.CreditCard;

import java.math.BigDecimal;


public class CreditCardDto {

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private BigDecimal limit;
    private BigDecimal restOfLimit;
    private CardType cardType;

    public CreditCardDto(String cardNumber, String cardHolderName, String expiryDate, String cvv, BigDecimal limit, BigDecimal restOfLimit, CardType cardType) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.limit = limit;
        this.restOfLimit = restOfLimit;
        this.cardType = cardType;
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
                creditCard.getCvv(),
                creditCard.getLimit(),
                creditCard.getRestOfLimit(), creditCard.getCardType());
    }

    public BigDecimal getRestOfLimit() {
        return restOfLimit;
    }

    public void setRestOfLimit(BigDecimal restOfLimit) {
        this.restOfLimit = restOfLimit;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
}
