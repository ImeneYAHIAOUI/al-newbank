package groupB.newbankV5.mockcreditcardnetwork.controllers;

import groupB.newbankV5.mockcreditcardnetwork.components.CreditCardAuthorizer;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardInformationDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.PaymentResponseDto;
import groupB.newbankV5.mockcreditcardnetwork.exceptions.ExpirationDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = PaymentAuthorizationController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class PaymentAuthorizationController {

    public static final String BASE_URI = "/api/payment";

    private final CreditCardAuthorizer creditCardAuthorizer;

    @Autowired
    public PaymentAuthorizationController(CreditCardAuthorizer creditCardAuthorizer) {
        this.creditCardAuthorizer = creditCardAuthorizer;
    }

    @PostMapping("/authorize")
    public ResponseEntity<PaymentResponseDto> authorizePayment(@RequestBody CreditCardInformationDto creditCardInformationDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(creditCardAuthorizer.AuthorizePayment(creditCardInformationDto));
        }
        catch (ExpirationDateException e) {
            PaymentResponseDto errorResponse = new PaymentResponseDto(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}
