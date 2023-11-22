package groupB.newbankV5.paymentsettlement.connectors;

import groupB.newbankV5.paymentsettlement.entities.Transaction;
import groupB.newbankV5.paymentsettlement.interfaces.FeesCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Component
public class FeesCalculatorProxy implements FeesCalculator {

    private static final Logger log = Logger.getLogger(ExternalBankProxy.class.getName());
    ParameterizedTypeReference<List<Transaction>> responseType = new ParameterizedTypeReference<List<Transaction>>() {};


    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${feesCalculator.host.baseurl:}")
    private String feesCalculatorHostandPort;

    @Override
    public Transaction[] calculateFees(Transaction[] transactions) {
        log.info("\u001B[35mSending fees calculation request to fees calculator\u001B[0m");
        try {
             return restTemplate.postForEntity(feesCalculatorHostandPort + "/api/feescalculator/calculate", transactions, Transaction[].class).getBody();

        } catch (Exception e) {
            log.warning("Error calculating fees: " + e.getMessage());
            return null;
        }
    }

}
