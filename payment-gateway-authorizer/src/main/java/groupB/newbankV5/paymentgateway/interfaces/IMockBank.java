package groupB.newbankV5.paymentgateway.interfaces;

import java.math.BigDecimal;

public interface IMockBank {
    String reserveFunds(BigDecimal amount, String cardNumber, String expiryDate, String cvv);
}
