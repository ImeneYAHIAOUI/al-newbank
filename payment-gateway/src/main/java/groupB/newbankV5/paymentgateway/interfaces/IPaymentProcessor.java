package groupB.newbankV5.paymentgateway.interfaces;

import java.math.BigDecimal;

public interface IPaymentProcessor {
    String reserveFunds(BigDecimal amount, String cardNumber, String expiryDate, String cvv);
}
