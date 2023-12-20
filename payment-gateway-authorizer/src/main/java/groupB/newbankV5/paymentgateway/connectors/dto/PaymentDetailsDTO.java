package groupB.newbankV5.paymentgateway.connectors.dto;

import java.math.BigDecimal;

public class PaymentDetailsDTO {
    private String cardHolderName;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private double amount;


    public PaymentDetailsDTO() {
        // Default constructor
    }

    public PaymentDetailsDTO(String cardNumber, String expirationDate, String cvv, double amount) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.amount = amount;
    }

    public PaymentDetailsDTO(String cardHolderName, String cardNumber, String expirationDate, String cvv) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.amount = amount;
    }

    public PaymentDetailsDTO(String cardHolderName, String cardNumber, String expirationDate, String cvv, double amount) {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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
