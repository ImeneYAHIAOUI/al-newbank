package groupB.newbankV5.feescalculator.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groupB.newbankV5.feescalculator.components.Calculator;
import groupB.newbankV5.feescalculator.config.KafkaProducerService;
import groupB.newbankV5.feescalculator.entities.Transaction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = FeesCalculatorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class FeesCalculatorController {
    private static final Logger log = LoggerFactory.getLogger(FeesCalculatorController.class);
    public static final String BASE_URI = "/api/feescalculator";
    private final KafkaProducerService kafkaProducerService;

    private final Calculator calculator;
    private final ObjectMapper objectMapper;

    @Autowired
    public FeesCalculatorController(Calculator calculator, KafkaProducerService kafkaProducerService, ObjectMapper objectMapper) {
        this.calculator = calculator;
        this.kafkaProducerService = kafkaProducerService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Health check");
        return ResponseEntity.ok().body("OK");
    }

    @KafkaListener(topics = "topic-fees-calculator", groupId = "group_id")
    public void receiveTransaction(@Payload String payload, ConsumerRecord<String, Transaction> cr) {
        // Process the received message
        try {
            Transaction transaction = objectMapper.readValue(payload, Transaction.class);
            log.info("Received transaction: " + transaction.toString());
            // Save a new one or save with the updated status
            calculator.applyFees(transaction);
        } catch (JsonProcessingException e) {
            log.error("Error processing transaction: " + e.getMessage());
        }

    }

}
