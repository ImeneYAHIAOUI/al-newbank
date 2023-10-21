package groupB.newbankV5.paymentsettlement.connectors;

import groupB.newbankV5.paymentsettlement.entities.Transaction;
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

    public Transaction[] getTransactionsToSettle() {
        log.info("Getting transactions to settle " );
        try{
            log.info("port and host "+ transactionHostandPort);
            return restTemplate.getForEntity(transactionHostandPort + "/api/transactions/toSettle",
                    Transaction[].class).getBody();
        } catch (Exception e) {
            log.warning("Error getting balance for getting transaction: " + e.getMessage());
            return null;
        }
    }

    public void putTransactionsToSettle(Transaction[] transactions) {
        log.info("editing transactions " );
        try{
            log.info("port and host "+ transactionHostandPort);
              restTemplate.put(transactionHostandPort + "/api/transactions/settle", transactions,
                     String.class);
            log.info("saved successfullt");
        } catch (Exception e) {
            log.warning("Error getting getting transactions " + e.getMessage());
        }
    }




}
