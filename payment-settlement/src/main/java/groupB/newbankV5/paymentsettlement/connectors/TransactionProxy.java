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
        try{
            return restTemplate.getForEntity(transactionHostandPort + "/api/transactions/toSettle",
                    Transaction[].class).getBody();
        } catch (Exception e) {
            log.warning("Error getting balance for getting transaction: " + e.getMessage());
            return null;
        }
    }

    public void putTransactionsToSettle(Transaction[] transactions) {
        try{
              restTemplate.put(transactionHostandPort + "/api/transactions/settle", transactions,
                     String.class);
              log.info("\u001B[35mTransactions saved\u001B[0m");
        } catch (Exception e) {
            log.warning("\u001B[31mError saving transactions: \u001B[0m" + e.getMessage());
        }
    }




}
