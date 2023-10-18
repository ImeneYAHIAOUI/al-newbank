package groupB.newbankV5.feescalculator.controllers;

import groupB.newbankV5.feescalculator.entities.Account;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = FeesCalculatorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class FeesCalculatorController {
    private static final Logger log = Logger.getLogger(FeesCalculatorController.class.getName());
    public static final String BASE_URI = "/api/feescalculator";
    private final KafkaTemplate kafkaTemplate;

    public FeesCalculatorController(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Health check");
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping
    public String sentMessage(@RequestBody Account account) {
        this.kafkaTemplate.send("transaction-1", new Account(account.getHolder(), account.getFunds()));
        return "Hello World!";
    }
    @KafkaListener(topics = "transaction-1")
    public void listener(@Payload Account account, ConsumerRecord<String, Account> cr) {
        log.info("Topic [transaction-1] Received message from {} with {} PLN "+ account.getHolder() + " "+ account.getFunds());
        log.info(cr.toString());
    }

}
