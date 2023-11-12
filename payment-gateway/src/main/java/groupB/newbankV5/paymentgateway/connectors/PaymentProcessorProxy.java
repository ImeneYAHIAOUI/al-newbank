package groupB.newbankV5.paymentgateway.connectors;

import groupB.newbankV5.paymentgateway.connectors.dto.ReserveFundsDto;
import groupB.newbankV5.paymentgateway.entities.Transaction;
import groupB.newbankV5.paymentgateway.interfaces.IPaymentProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Component
public class PaymentProcessorProxy implements IPaymentProcessor {
    private static final Logger log = Logger.getLogger(PaymentProcessorProxy.class.getName());
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${paymentprocessor.host.baseurl:}")
    private String paymentProcessorHostandPort;
    @Override
    public String reserveFunds(Transaction transaction) {
        try {
            return restTemplate.postForEntity(paymentProcessorHostandPort + "/api/payment/reserveFunds",
                    transaction, String.class).getBody();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
