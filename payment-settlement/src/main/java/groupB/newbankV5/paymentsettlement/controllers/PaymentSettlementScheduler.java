package groupB.newbankV5.paymentsettlement.controllers;

import groupB.newbankV5.paymentsettlement.components.SettlePayment;
import groupB.newbankV5.paymentsettlement.connectors.TransactionProxy;
import groupB.newbankV5.paymentsettlement.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = PaymentSettlementScheduler.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class PaymentSettlementScheduler {

    private static final Logger log = Logger.getLogger(PaymentSettlementScheduler.class.getName());
    public static final String BASE_URI = "/api/settle";

    @Autowired
    private TransactionProxy transactionProxy;

    @Autowired
    private SettlePayment settlePayment;

    @PostMapping
    @Scheduled(cron = "*/10 * * * * *")
    public ResponseEntity<String> process() {
        Transaction[] transactions = transactionProxy.getTransactionsToSettle();
        if(transactions.length == 0) {
            log.info("PROCESS SETTLEMENT - NO TRANSACTIONS TO SETTLE");
            return ResponseEntity.status(HttpStatus.OK).body("NO TRANSACTIONS TO SETTLE");
        }
        log.info("\u001B[35mPROCESS SETTLEMENT - START\u001B[0m");
        settlePayment.settlePayments();
        log.info("\u001B[35mPROCESS SETTLEMENT - END\u001B[0m");

        return ResponseEntity.status(HttpStatus.OK).body("DONE");
    }

}
