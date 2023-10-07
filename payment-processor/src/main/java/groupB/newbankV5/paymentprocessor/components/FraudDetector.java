package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Component
public class FraudDetector {
    private static final Logger log = Logger.getLogger(FraudDetector.class.getName());

    private static final BigDecimal HIGH_TRANSACTION_THRESHOLD = new BigDecimal(1000);

    private static final BigDecimal LOW_TRANSACTION_THRESHOLD = new BigDecimal(0);


    public boolean isFraudulent(PaymentDetailsDTO transaction) {
        log.info("Checking for fraudulent transaction");
        BigDecimal amount = transaction.getAmount();

        if (amount.compareTo(HIGH_TRANSACTION_THRESHOLD) > 0) {
            log.info("The amount is high enough to be considered a fraud risk");
            return true;
        }

        if (amount.compareTo(LOW_TRANSACTION_THRESHOLD) < 0) {
            log.info("The amount is low enough to be considered a fraud risk");
            return true;
        }

        return false;
    }

}
