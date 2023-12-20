package groupB.newbankV5.mockcreditcardnetwork.controllers;

import groupB.newbankV5.mockcreditcardnetwork.components.CreditCardAuthorizer;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardInformationDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardCheckResponseDto;
import groupB.newbankV5.mockcreditcardnetwork.exceptions.ExpirationDateException;
import groupB.newbankV5.mockcreditcardnetwork.exceptions.InvalidCardInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CreditCardVerificationController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CreditCardVerificationController {
    private static final Logger log = Logger.getLogger(CreditCardVerificationController.class.getName());

    public static final String BASE_URI = "/api/payment";

    private final CreditCardAuthorizer creditCardAuthorizer;

    @Autowired
    public CreditCardVerificationController(CreditCardAuthorizer creditCardAuthorizer) {
        this.creditCardAuthorizer = creditCardAuthorizer;
    }

    @PostMapping("authorize")
    public ResponseEntity<CreditCardCheckResponseDto> authorizePayment(@RequestBody CreditCardInformationDto creditCardInformationDto) {
        try {
            log.info("\u001B[32mReceived credit card verification request\u001B[0m");
            return ResponseEntity.status(HttpStatus.OK).body(creditCardAuthorizer.ValidateCreditCard(creditCardInformationDto));
        } catch (InvalidCardInformation e) {
            log.warning("\u001B[31mInvalid card information\u001B[0m");
            CreditCardCheckResponseDto errorResponse = new CreditCardCheckResponseDto();
            errorResponse.setResponse(false);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setAuthToken();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
