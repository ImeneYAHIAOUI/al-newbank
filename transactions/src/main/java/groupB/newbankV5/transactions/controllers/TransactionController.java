package groupB.newbankV5.transactions.controllers;

import groupB.newbankV5.transactions.entities.Trans;
import groupB.newbankV5.transactions.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demo")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping
    public Trans create(@RequestBody Trans transaction) {
        return transactionRepository.save(transaction);
    }

    @GetMapping
    public List<Trans> get() {
        return transactionRepository.findAll();
    }
}
