package groupB.newbankV5.metrics.entities;


import java.io.Serializable;
import java.util.Objects;

public class CreditCard implements Serializable {

    private String cardNumber;
    private String expiryDate;
    private String cvv;



    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, expiryDate, cvv);
    }


    public CreditCard(String cardNumber, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public CreditCard() {

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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
}
