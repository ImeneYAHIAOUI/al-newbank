package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FraudDetector {
    private static final BigDecimal HIGH_TRANSACTION_THRESHOLD = new BigDecimal(1000);

    private static final BigDecimal LOW_TRANSACTION_THRESHOLD = new BigDecimal(0);


    public boolean isFraudulent(PaymentDetailsDTO transaction) {
        BigDecimal amount = transaction.getAmount();

        if (amount.compareTo(HIGH_TRANSACTION_THRESHOLD) > 0) {
            return true;
        }

        if (amount.compareTo(LOW_TRANSACTION_THRESHOLD) < 0) {
            return true;
        }

        return false;
    }

}
