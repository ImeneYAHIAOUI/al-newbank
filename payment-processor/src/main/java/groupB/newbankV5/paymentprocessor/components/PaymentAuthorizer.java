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

    private final CreditCardNetworkProxy creditCardNetworkProxy;
    private final FraudDetector fraudDetector;

    @Autowired
    public PaymentAuthorizer(CreditCardNetworkProxy creditCardNetworkProxy, FraudDetector fraudDetector) {
        this.creditCardNetworkProxy = creditCardNetworkProxy;
        this.fraudDetector = fraudDetector;
    }
    public PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails) {
        log.info("Authorizing payment");
        if(fraudDetector.isFraudulent(paymentDetails)) {
            return new PaymentResponseDto(false, "Fraudulent transaction");
        }
        CcnResponseDto ccnResponseDto =  creditCardNetworkProxy.authorizePayment(paymentDetails);
        if(ccnResponseDto.getResponse()) {
            return new PaymentResponseDto(true, "Payment authorized");
        }
        return new PaymentResponseDto(false, "Payment declined : "+ccnResponseDto.getMessage());
    }
}
