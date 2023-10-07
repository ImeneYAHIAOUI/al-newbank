package groupB.newbankV5.mockcreditcardnetwork.controllers;

import groupB.newbankV5.mockcreditcardnetwork.components.CreditCardAuthorizer;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardInformationDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.ResponseDto;
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
@RequestMapping(path = CreditCardNetworkController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CreditCardNetworkController {

    public static final String BASE_URI = "/api/payment";

    private final CreditCardAuthorizer creditCardAuthorizer;

    @Autowired
    public CreditCardNetworkController(CreditCardAuthorizer creditCardAuthorizer) {
        this.creditCardAuthorizer = creditCardAuthorizer;
    }

    @PostMapping("/authorize")
    public ResponseEntity<ResponseDto> authorizePayment(@RequestBody CreditCardInformationDto creditCardInformationDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(creditCardAuthorizer.AuthorizePayment(creditCardInformationDto));
        }
        catch (ExpirationDateException e) {
            ResponseDto errorResponse = new ResponseDto(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}
