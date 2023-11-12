package groupB.newbankV5.paymentgateway.connectors;

import groupB.newbankV5.paymentgateway.connectors.dto.ReserveFundsDto;
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
    public String reserveFunds(BigDecimal amount, String cardNumber, String expiryDate, String cvv, String paymentToken) {
        try {
            ReserveFundsDto reserveFundsDto = new ReserveFundsDto( amount, cardNumber, expiryDate, cvv, paymentToken);
            return restTemplate.postForEntity(paymentProcessorHostandPort + "/api/payment/reserveFunds",
                    reserveFundsDto, String.class).getBody();
        } catch (Exception e) {
            log.info("\u001B[31mError: \u001B[30m" + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
