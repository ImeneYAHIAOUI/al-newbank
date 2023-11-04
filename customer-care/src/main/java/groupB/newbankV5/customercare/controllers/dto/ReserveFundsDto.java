package groupB.newbankV5.customercare.controllers.dto;


import java.math.BigDecimal;

public class ReserveFundsDto {
    BigDecimal amount;
    private String cardNumber;
    private String expirationDate;
    private String cvv;

    public ReserveFundsDto() {

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


}
