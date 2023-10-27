package groupB.newbankV5.paymentprocessor.connectors.dto;



import java.math.BigDecimal;


public class CreditCardDto {

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private BigDecimal limit;
    private BigDecimal restOfLimit;
    private String cardType;

    public CreditCardDto() {
    }
    public CreditCardDto(String cardNumber, String cardHolderName, String expiryDate, String cvv, BigDecimal limit, BigDecimal restOfLimit, String cardType) {
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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
