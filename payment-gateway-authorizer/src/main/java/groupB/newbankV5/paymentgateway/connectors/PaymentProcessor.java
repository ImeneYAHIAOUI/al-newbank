package groupB.newbankV5.paymentgateway.connectors;

import groupB.newbankV5.paymentgateway.entities.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component

public class PaymentProcessor  {
    private static final Logger log = Logger.getLogger(PaymentProcessor.class.getName());

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${paymentProcessor.host.baseurl:}")
    private String paymentProcessorHostandPort;

    public void saveTransactions(Transaction[] transactions){
        try {
            restTemplate.postForEntity(paymentProcessorHostandPort + "/api/payment/batchSaveTransactions",transactions,
                    Transaction[].class).getBody();
        } catch (Exception e) {
            log.warning("\u001B[31mError saving transactions: \u001B[0m" + e.getMessage());
        }
    }
}
