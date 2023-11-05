package groupB.newbankV5.anaytics.controllers;

import groupB.newbankV5.anaytics.components.AnayticsService;
import groupB.newbankV5.anaytics.entities.ClientAnalytics;
import groupB.newbankV5.anaytics.entities.MerchantAnalytics;
import groupB.newbankV5.anaytics.entities.BankAccount;
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
    public List<MerchantAnalytics> getForMerchant(@RequestBody BankAccount bankAccount) {
        return anayticsService.analyseMerchantBenifitsPerDay(bankAccount.getIBAN(), bankAccount.getBIC());
    }

    @GetMapping("/customer")
    public ClientAnalytics getForCustomer(@RequestBody BankAccount bankAccount, @RequestParam int year,
            @RequestParam int month){
        return anayticsService.clientAnalytics(bankAccount, year, month);
    }
}
