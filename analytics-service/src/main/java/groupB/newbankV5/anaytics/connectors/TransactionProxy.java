package groupB.newbankV5.anaytics.connectors;

import groupB.newbankV5.anaytics.entities.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class TransactionProxy {
    private static final Logger log = Logger.getLogger(TransactionProxy.class.getName());

    @Value("${transaction.host.baseurl:}")
    private String transactionHostandPort;
    private RestTemplate restTemplate = new RestTemplate();

    public Transaction[] findAll() {
        log.info("Getting transactions to settle " );
        try{
            return restTemplate.getForEntity(transactionHostandPort + "/api/transactions",
                    Transaction[].class).getBody();
        } catch (Exception e) {
            log.warning("Error getting transactions : " + e.getMessage());
            return null;
        }
    }

}
