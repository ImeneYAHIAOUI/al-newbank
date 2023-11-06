package groupB.newbankV5.externalbank.components.dtos;

import java.math.BigDecimal;

public class CreditCardInformationDto {

    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private BigDecimal amount;

    public CreditCardInformationDto() {
    }

    public CreditCardInformationDto(String cardNumber, String expirationDate, String cvv, BigDecimal amount) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.amount = amount;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return "CreditCardInformationDto{" +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
