package groupB.newbankV5.paymentgateway.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groupB.newbankV5.paymentgateway.connectors.PaymentProcessorProxy;
import groupB.newbankV5.paymentgateway.entities.Transaction;
import groupB.newbankV5.paymentgateway.interfaces.ITransactionProcessor;
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
@RequestMapping(path = TransactionerController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class TransactionerController {
    private static final Logger log = Logger.getLogger(TransactionerController.class.getName());
    public static final String BASE_URI = "/api/gateway-confirmation";
    private final ITransactionProcessor transactionProcessor;
    private final PaymentProcessorProxy paymentProcessorProxy;

    private final ObjectMapper objectMapper;

    @Autowired
    public TransactionerController(ITransactionProcessor transactionProcessor,
            PaymentProcessorProxy paymentProcessorProxy, ObjectMapper objectMapper) {
        this.transactionProcessor = transactionProcessor;
        this.paymentProcessorProxy = paymentProcessorProxy;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "confirmation-topic", groupId = "confirmation_id")
    public void receiveTransaction(@Payload String payload, ConsumerRecord<String, String> cr) {
        try {
            String transactionId = objectMapper.readValue(payload, String.class);
            log.info("\u001B[32mReceived transaction: " + transactionId + "\u001B[0m");

        } catch (JsonProcessingException e) {
            log.info("error here");
        }
    }

    @PostMapping("{transactionId}")
    public ResponseEntity<String> confirmPayment(@PathVariable UUID transactionId) {
        String resp = transactionProcessor.confirmPayment(transactionId);
        log.info("\u001B[32mPayment confirmed\u001B[0m");
        return ResponseEntity.status(202).body(resp);
    }

    @GetMapping
    public void test(){
        paymentProcessorProxy.reserveFunds(new Transaction());    }

}