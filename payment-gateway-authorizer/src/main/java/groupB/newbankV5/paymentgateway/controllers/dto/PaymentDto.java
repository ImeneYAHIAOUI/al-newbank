package groupB.newbankV5.paymentgateway.controllers.dto;

import java.math.BigDecimal;

public class PaymentDto {
    private double amount;
    private String encryptedCard;

    public PaymentDto() {
        // Default constructor
    }





    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getEncryptedCard() {
        return encryptedCard;
    }

    public void setEncryptedCard(String encryptedCard) {
        this.encryptedCard = encryptedCard;
    }
}
