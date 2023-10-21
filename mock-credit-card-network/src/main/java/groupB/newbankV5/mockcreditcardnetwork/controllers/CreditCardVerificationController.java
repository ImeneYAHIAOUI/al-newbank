package groupB.newbankV5.mockcreditcardnetwork.controllers;

import groupB.newbankV5.mockcreditcardnetwork.components.CreditCardAuthorizer;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardInformationDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardCheckResponseDto;
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
@RequestMapping(path = CreditCardVerificationController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CreditCardVerificationController {

    public static final String BASE_URI = "/api/payment";

    private final CreditCardAuthorizer creditCardAuthorizer;

    @Autowired
    public CreditCardVerificationController(CreditCardAuthorizer creditCardAuthorizer) {
        this.creditCardAuthorizer = creditCardAuthorizer;
    }

    @PostMapping("authorize")
    public ResponseEntity<CreditCardCheckResponseDto> authorizePayment(@RequestBody CreditCardInformationDto creditCardInformationDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(creditCardAuthorizer.ValidateCreditCard(creditCardInformationDto));
        }
        catch (ExpirationDateException e) {
            CreditCardCheckResponseDto errorResponse = new CreditCardCheckResponseDto();
            errorResponse.setResponse(false);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setAuthToken();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

}
