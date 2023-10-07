package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class PaymentAuthorizer {
    private static final Logger log = Logger.getLogger(PaymentAuthorizer.class.getName());

    private final FraudDetector fraudDetector;
    private final FundsHandler fundsHandler;

    @Autowired
    public PaymentAuthorizer(FraudDetector fraudDetector, FundsHandler fundsHandler) {
        this.fraudDetector = fraudDetector;
        this.fundsHandler = fundsHandler;
    }
    public PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails) {
        log.info("Authorizing payment");
        if(fraudDetector.isFraudulent(paymentDetails) ) {
            return new PaymentResponseDto(false, "Fraudulent transaction");
        }
        if (!fundsHandler.hasSufficientFunds(paymentDetails)) {
            return new PaymentResponseDto(false, "Insufficient funds");
        }

        return new PaymentResponseDto(true, "Payment authorized");

    }
}
