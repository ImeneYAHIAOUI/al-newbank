package groupB.newbankV5.transactions.controllers;

import groupB.newbankV5.transactions.entities.Transaction;
import groupB.newbankV5.transactions.interfaces.IPersister;
import groupB.newbankV5.transactions.repositories.TransactionRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private IPersister persister;

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

    @KafkaListener(topics = "topic-transactions", groupId = "group_id")
    public void receiveTransaction(@Payload Transaction transaction, ConsumerRecord<String, Transaction> cr) {
        // Process the received message
        log.info("Received transaction: " + transaction.toString());
        // Save a new one or save with the updated status
        persister.saveTransaction(transaction);
    }
}
