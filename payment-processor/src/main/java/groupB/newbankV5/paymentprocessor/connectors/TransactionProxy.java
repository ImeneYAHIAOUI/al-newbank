package groupB.newbankV5.paymentprocessor.connectors;

import groupB.newbankV5.paymentprocessor.entities.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class TransactionProxy {

    private static final Logger log = Logger.getLogger(TransactionProxy.class.getName());

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${transaction.host.baseurl:}")
    private String transactionHostandPort;


    public Transaction[] getWeekTransaction(String iban) {

        log.info("getting weekly transaction");
        try {
            return restTemplate.getForEntity(transactionHostandPort + "/api/transactions/weekly?iban="+ iban,
                    Transaction[].class).getBody();
        } catch (Exception e) {
            log.warning("\u001B[31mError getting weekly transaction: \u001B[0m" + e.getMessage());
            return null;
        }
    }

}