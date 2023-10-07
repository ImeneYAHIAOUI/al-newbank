package groupB.newbankV5.paymentprocessor.controllers;

import groupB.newbankV5.paymentprocessor.components.PaymentAuthorizer;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = PaymentProcessorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class PaymentProcessorController {
    public static final String BASE_URI = "/api/payment";

    private final PaymentAuthorizer paymentAuthorizer;

    @Autowired
    public PaymentProcessorController(PaymentAuthorizer paymentAuthorizer) {
        this.paymentAuthorizer = paymentAuthorizer;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDto> processPayment(@RequestBody PaymentDetailsDTO paymentDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentAuthorizer.authorizePayment(paymentDetails));

    }

}
