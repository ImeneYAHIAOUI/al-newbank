package groupB.newbankV5.paymentgateway.connectors.dto;


import java.math.BigDecimal;

public class ReserveFundsDto {
    private BigDecimal amount;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private String authToken;

    public ReserveFundsDto() {

    }

    public ReserveFundsDto(BigDecimal amount, String cardNumber, String expirationDate, String cvv, String authToken) {
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.authToken = authToken;
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


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
