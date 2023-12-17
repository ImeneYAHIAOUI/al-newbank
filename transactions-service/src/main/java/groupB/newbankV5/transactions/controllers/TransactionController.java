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

import java.time.LocalDateTime;
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
        List<Transaction>  transactions = transactionRepository.findAll().stream()
                    .filter(transaction -> !transaction.getStatus().equals(TransactionStatus.SETTLED))
                    .collect(Collectors.toList());
        if(transactions.size() > 0)
            log.info("\u001B[35mSending " + transactions.size() + " transaction" + (transactions.size() > 1 ? "s" : "") + " to settle\u001B[0m");
        return transactions;
    }

    @PutMapping("settle")
    public ResponseEntity<String> saveTransactions(@RequestBody List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
        return ResponseEntity.ok("Transactions saved successfully");
    }

    @GetMapping("/weekly")
    public List<Transaction> get(@RequestParam("iban") String iban) {
        LocalDateTime localDateTime = LocalDateTime.now().minus(7, java.time.temporal.ChronoUnit.DAYS);
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getSender().getIBAN().equals(iban))
                .filter(transaction -> !transaction.getStatus().equals(TransactionStatus.FAILED))
                .filter(transaction -> transaction.getTime().isAfter(localDateTime))
                .collect(Collectors.toList());
    }

    @KafkaListener(topics = "topic-transactions", groupId = "group_id")
    public void receiveTransaction(@Payload String payload, ConsumerRecord<String, Transaction> cr) {
        // Process the received message
        try {
            Transaction transaction = objectMapper.readValue(payload, Transaction.class);
            log.info("\u001B[34mReceived transaction from Kafka\u001B[0m");

            // Save a new one or save with the updated status
            transactionRepository.save(transaction);
            log.info("\u001B[34mTransaction saved\u001B[0m");
        } catch (JsonProcessingException e) {
            log.error("\u001B[31mError while processing transaction: " + e.getMessage() + "\u001B[0m");
        }
    }
}
