package groupB.newbankV5.externalbank.controllers;

import groupB.newbankV5.externalbank.components.DepositAuthorizer;
import groupB.newbankV5.externalbank.controllers.dto.AuthorizeDto;
import groupB.newbankV5.externalbank.controllers.dto.TransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = DepositAuthorizerController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class DepositAuthorizerController {

    @Autowired
    private DepositAuthorizer depositAuthorizer;

    public static final String BASE_URI = "/api/externalbank";

    @PostMapping("/authorize")
    public ResponseEntity<AuthorizeDto> authorizeDeposit(@RequestBody TransferDto transferDto) {
        return ResponseEntity.ok(depositAuthorizer.authorize(transferDto));
    }
}
