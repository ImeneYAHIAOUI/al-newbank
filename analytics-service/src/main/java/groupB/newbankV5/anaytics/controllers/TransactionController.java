package groupB.newbankV5.anaytics.controllers;

import groupB.newbankV5.anaytics.components.AnayticsService;
import groupB.newbankV5.anaytics.entities.AmountReceivedPerDay;
import groupB.newbankV5.anaytics.entities.BankAccount;
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

    private final AnayticsService anayticsService;

    @Autowired
    public TransactionController(AnayticsService transactionRepository) {
        this.anayticsService = transactionRepository;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/merchant")
    public List<AmountReceivedPerDay> getForCustomer(@RequestBody BankAccount bankAccount) {
        return anayticsService.analyseMerchantBenifitsPerDay(bankAccount.getIBAN(), bankAccount.getBIC());
    }
    

}
