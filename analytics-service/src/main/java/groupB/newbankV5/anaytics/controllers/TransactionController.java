package groupB.newbankV5.anaytics.controllers;

import groupB.newbankV5.anaytics.entities.Transaction;
import groupB.newbankV5.anaytics.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class TransactionController {

    private final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping
    public List<Transaction> get() {
        return transactionRepository.findAll();
    }



}
