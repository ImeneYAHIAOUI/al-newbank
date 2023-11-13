package groupB.newbankV5.anaytics.controllers;

import groupB.newbankV5.anaytics.components.AnayticsService;
import groupB.newbankV5.anaytics.entities.ClientAnalytics;
import groupB.newbankV5.anaytics.entities.MerchantAnalytics;
import groupB.newbankV5.anaytics.entities.BankAccount;
import groupB.newbankV5.anaytics.exceptions.MerchantNotFoundException;
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

    @GetMapping("")
    public void getForMerchant() {

    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }


    @GetMapping("/merchant")
    public List<MerchantAnalytics> getForMerchant(@RequestParam String name) throws MerchantNotFoundException {
        return anayticsService.analyseMerchantBenifitsPerDay(name);
    }

    @GetMapping("/customer")
    public ClientAnalytics getForCustomer(@RequestParam("bic") String bic, @RequestParam("iban") String iban, @RequestParam int year,
            @RequestParam int month){
        BankAccount bankAccount = new BankAccount(iban, bic);
        return anayticsService.clientAnalytics(bankAccount, year, month);
    }
}
