package groupB.newbankV5.paymentgateway.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = FeesCalculatorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class FeesCalculatorController {
    private static final Logger log = Logger.getLogger(FeesCalculatorController.class.getName());
    public static final String BASE_URI = "/api/feescalculator";

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Health check");
        return ResponseEntity.ok().body("OK");
    }

}
