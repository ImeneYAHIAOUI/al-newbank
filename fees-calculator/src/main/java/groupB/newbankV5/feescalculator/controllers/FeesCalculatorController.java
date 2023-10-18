package groupB.newbankV5.feescalculator.controllers;

import groupB.newbankV5.feescalculator.components.Calculator;
import groupB.newbankV5.feescalculator.config.KafkaProducerService;
import groupB.newbankV5.feescalculator.entities.Account;
import groupB.newbankV5.feescalculator.entities.BankAccount;
import groupB.newbankV5.feescalculator.entities.Merchant;
import groupB.newbankV5.feescalculator.entities.Transaction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = FeesCalculatorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class FeesCalculatorController {
    private static final Logger log = Logger.getLogger(FeesCalculatorController.class.getName());
    public static final String BASE_URI = "/api/feescalculator";
    private final KafkaProducerService kafkaProducerService;

    private final Calculator calculator;

    @Autowired
    public FeesCalculatorController(Calculator calculator, KafkaProducerService kafkaProducerService) {
        this.calculator = calculator;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Health check");
        return ResponseEntity.ok().body("OK");
    }

    @KafkaListener(topics = "topic-transactions", groupId = "group_id")
    public void receiveTransaction(@Payload Transaction transaction, ConsumerRecord<String, Transaction> cr) {
        // Process the received message
        log.info("Received transaction: " + transaction.toString());
        // Calculate the fees
        calculator.applyFees(transaction);
    }

}
