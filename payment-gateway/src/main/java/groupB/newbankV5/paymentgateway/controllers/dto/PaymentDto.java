package groupB.newbankV5.paymentgateway.controllers.dto;

import groupB.newbankV5.paymentgateway.entities.CreditCard;

import java.math.BigDecimal;

public class PaymentDto {
    private String token;
    private BigDecimal amount;
    private byte[] cryptedCreditCard;

    public PaymentDto() {
        // Default constructor
    }

    public PaymentDto(String token, BigDecimal amount, byte[] cryptedCreditCard) {
        this.token = token;
        this.amount = amount;
        this.cryptedCreditCard = cryptedCreditCard;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public byte[] getCryptedCreditCard() {
        return cryptedCreditCard;
    }

    public void setCryptedCreditCard(byte[] cryptedCreditCard) {
        this.cryptedCreditCard = cryptedCreditCard;
    }
}
