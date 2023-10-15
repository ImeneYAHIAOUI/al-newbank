package groupB.newbankV5.paymentgateway.controllers.dto;

import groupB.newbankV5.paymentgateway.entities.CreditCard;

import java.math.BigDecimal;

public class PaymentDto {
    private String token;
    private BigDecimal amount;
    private CreditCard creditCard;

    public PaymentDto() {
        // Default constructor
    }

    public PaymentDto(String token, BigDecimal amount, CreditCard creditCard) {
        this.token = token;
        this.amount = amount;
        this.creditCard = creditCard;
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

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}
