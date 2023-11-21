package groupB.newbankV5.metrics.controllers;

import groupB.newbankV5.metrics.components.MetricsService;
import groupB.newbankV5.metrics.entities.ClientAnalytics;
import groupB.newbankV5.metrics.entities.MerchantAnalytics;
import groupB.newbankV5.metrics.entities.BankAccount;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;
import groupB.newbankV5.metrics.exceptions.MerchantNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final Logger log = LoggerFactory.getLogger(MetricsController.class);

    private final MetricsService metricsService;

    @Autowired
    public MetricsController(MetricsService metrics) {
        this.metricsService = metrics;
    }

    @GetMapping("")
    public void getForMerchant() {

    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
    @GetMapping("/transactions/confirmed")
    public ResponseEntity<Long> getConfirmedTransaction(@RequestHeader("Authorization") String authorizationHeader ) throws InvalidTokenException,
            ApplicationNotFoundException {
        log.info("\u001B[32m getting confirmed transactions\u001B[0m");
        String token = authorizationHeader.substring(7);
        long number = metricsService.getConfirmedTransaction(token);
        return ResponseEntity.status(200).body(number);
    }
    @GetMapping("/transactions/authorized")
    public ResponseEntity<Long> getAuthorizedTransaction( @RequestHeader("Authorization") String authorizationHeader ) throws InvalidTokenException,
            ApplicationNotFoundException {
        log.info("\u001B[32m getting authorized transactions\u001B[0m");
        String token = authorizationHeader.substring(7);
        long number = metricsService.getAuthorizedTransaction(token);
        return ResponseEntity.status(200).body(number);
    }

}
