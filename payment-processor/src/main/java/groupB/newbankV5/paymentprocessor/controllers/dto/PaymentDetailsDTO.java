package groupB.newbankV5.paymentprocessor.controllers.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentDetailsDTO {
    private String cardHolderName;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private BigDecimal amount;

    public PaymentDetailsDTO() {
        // Default constructor
    }

    public PaymentDetailsDTO(String cardHolderName, String cardNumber, String expirationDate, String cvv, BigDecimal amount) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.amount = amount;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
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

    @Override
    public String toString() {
        return "PaymentDetailsDTO{" +
                "cardHolderName='" + cardHolderName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate=" + expirationDate +
                ", cvv='" + cvv + '\'' +
                ", amount=" + amount +
                '}';
    }
}
