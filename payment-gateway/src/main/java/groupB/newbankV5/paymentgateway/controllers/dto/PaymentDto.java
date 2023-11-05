package groupB.newbankV5.paymentgateway.controllers.dto;

import java.math.BigDecimal;

public class PaymentDto {
    private BigDecimal amount;
    private String encryptedCard;

    public PaymentDto() {
        // Default constructor
    }

    public PaymentDto(String token, BigDecimal amount, String encryptedCard) {
        this.amount = amount;
        this.encryptedCard = encryptedCard;
    }



    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEncryptedCard() {
        return encryptedCard;
    }

    public void setEncryptedCard(String encryptedCard) {
        this.encryptedCard = encryptedCard;
    }
}
