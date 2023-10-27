package groupB.newbankV5.customercare.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "CreditCard_id", nullable = false)
    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    @Column(name = "card_limit")
    private BigDecimal limit;
    private BigDecimal restOfLimit;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "Account_id")
    private Account account;

    private CardType cardType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public CreditCard(String cardNumber, String cardHolderName, String expiryDate, String cvv,Account account, BigDecimal limit, CardType cardType) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.account = account;
        this.limit = limit;
        this.restOfLimit = limit;
        this.cardType = cardType;
    }

    public CreditCard() {

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

    @Override
    public String toString() {
        return "CreditCard{" +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getRestOfLimit() {
        return restOfLimit;
    }

    public void setRestOfLimit(BigDecimal restOfLimit) {
        this.restOfLimit = restOfLimit;
    }
}
