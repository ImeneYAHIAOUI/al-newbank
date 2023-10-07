package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.components.dto.CcnResponseDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class PaymentAuthorizer {
    private static final Logger log = Logger.getLogger(PaymentAuthorizer.class.getName());

    private final FraudDetector fraudDetector;

    @Autowired
    public PaymentAuthorizer( FraudDetector fraudDetector) {
        this.fraudDetector = fraudDetector;
    }
    public PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails) {
        log.info("Authorizing payment");
        if(fraudDetector.isFraudulent(paymentDetails)) {
            return new PaymentResponseDto(false, "Fraudulent transaction");
        }

        return new PaymentResponseDto(true, "Payment authorized");

    }
}
