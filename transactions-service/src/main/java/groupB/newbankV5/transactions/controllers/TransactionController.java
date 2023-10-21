package groupB.newbankV5.transactions.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groupB.newbankV5.transactions.entities.Transaction;
import groupB.newbankV5.transactions.entities.TransactionStatus;
import groupB.newbankV5.transactions.repositories.TransactionRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionRepository transactionRepository;


    private final ObjectMapper objectMapper;
    @Autowired
    public TransactionController(ObjectMapper objectMapper, TransactionRepository transactionRepository) {
        this.objectMapper = objectMapper;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @PostMapping
    public Transaction create(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @GetMapping
    public List<Transaction> get() {
        return transactionRepository.findAll();
    }

    @GetMapping("/toSettle")
    public List<Transaction> transactionToSettle(){
        log.info("Received request to get transactions to settle");
        return transactionRepository.findAll().stream()
                    .filter(transaction -> !transaction.getStatus().equals(TransactionStatus.SETTLED))
                    .collect(Collectors.toList());
    }

    @PutMapping("settle")
    public ResponseEntity<String> saveTransactions(@RequestBody List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
        return ResponseEntity.ok("Transactions saved successfully");
    }

    @KafkaListener(topics = "topic-transactions", groupId = "group_id")
    public void receiveTransaction(@Payload String payload, ConsumerRecord<String, Transaction> cr) {
        // Process the received message
        try {
            Transaction transaction = objectMapper.readValue(payload, Transaction.class);
            log.info("Received transaction: " + transaction.toString());
            // Save a new one or save with the updated status
            transactionRepository.save(transaction);
        } catch (JsonProcessingException e) {
            log.error("Error processing transaction: " + e.getMessage());
        }
    }
}
