package groupB.newbankV5.paymentgateway.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groupB.newbankV5.paymentgateway.connectors.PaymentProcessorProxy;
import groupB.newbankV5.paymentgateway.entities.Transaction;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentgateway.interfaces.ITransactionConfirmation;
import groupB.newbankV5.paymentgateway.interfaces.ITransactionFinder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = TransactionConfirmationController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class TransactionConfirmationController {
    private static final Logger log = Logger.getLogger(TransactionConfirmationController.class.getName());
    public static final String BASE_URI = "/api/gateway-confirmation";
    private final ITransactionConfirmation transactionProcessor;
    private final PaymentProcessorProxy paymentProcessorProxy;
    private final ITransactionFinder transactionFinder;
    private final ObjectMapper objectMapper;

    @Autowired
    public TransactionConfirmationController(ITransactionConfirmation transactionProcessor,
            PaymentProcessorProxy paymentProcessorProxy, ObjectMapper objectMapper, ITransactionFinder transactionFinder) {
        this.transactionProcessor = transactionProcessor;
        this.paymentProcessorProxy = paymentProcessorProxy;
        this.objectMapper = objectMapper;
        this.transactionFinder=transactionFinder;
    }
    @GetMapping("/transactions/confirmed/{id}")
    public ResponseEntity<Long> getConfirmedTransaction(@PathVariable("id") Long id ) throws InvalidTokenException,
            ApplicationNotFoundException {
        log.info("\u001B[32m getting confirmed transactions\u001B[0m");
        long number = transactionFinder.getConfirmedTransaction(id);
        return ResponseEntity.status(200).body(number);
    }


    @PostMapping("{transactionId}")
    public ResponseEntity<String> confirmPayment(@PathVariable UUID transactionId) {
        String resp = transactionProcessor.confirmPayment(transactionId);
        log.info("\u001B[32mPayment confirmed\u001B[0m");
        return ResponseEntity.status(202).body(resp);
    }

}