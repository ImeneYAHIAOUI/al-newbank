package groupB.newbankV5.feescalculator.controllers;

import groupB.newbankV5.feescalculator.components.Calculator;
import groupB.newbankV5.feescalculator.entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = FeesCalculatorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class FeesCalculatorController {
    private static final Logger log = LoggerFactory.getLogger(FeesCalculatorController.class);
    public static final String BASE_URI = "/api/feescalculator";

    private final Calculator calculator;

    @Autowired
    public FeesCalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    @GetMapping("health")
    public ResponseEntity<String> health() {
        log.info("Health check");
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("calculate")
    public ResponseEntity<List<Transaction>> calculateFees(@RequestBody List<Transaction> transactions) {
        log.info("\u001B[0m\u001B[35mReceived request to apply fees\u001B[0m");
        transactions.forEach(calculator::applyFees);


        log.info("\u001B[35mFees applied\u001B[0m");
        return ResponseEntity.ok().body(transactions);
    }
}
