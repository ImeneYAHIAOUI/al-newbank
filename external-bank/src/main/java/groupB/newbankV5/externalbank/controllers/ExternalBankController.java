package groupB.newbankV5.externalbank.controllers;

import groupB.newbankV5.externalbank.components.DepositAuthorizer;
import groupB.newbankV5.externalbank.components.SettlePaymentAccount;
import groupB.newbankV5.externalbank.components.dtos.CreditCardInformationDto;
import groupB.newbankV5.externalbank.controllers.dto.AuthorizeDto;
import groupB.newbankV5.externalbank.controllers.dto.IbanAmountDto;
import groupB.newbankV5.externalbank.controllers.dto.SettleDto;
import groupB.newbankV5.externalbank.controllers.dto.TransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = ExternalBankController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class ExternalBankController {

    @Autowired
    private DepositAuthorizer depositAuthorizer;

    @Autowired
    private SettlePaymentAccount settlePaymentAccount;

    private static final Logger log = Logger.getLogger(ExternalBankController.class.getName());


    public static final String BASE_URI = "/api/externalbank";

    @PostMapping("authorize")
    public ResponseEntity<AuthorizeDto> authorizeDeposit(@RequestBody TransferDto transferDto) {
        return ResponseEntity.ok(depositAuthorizer.authorize(transferDto));
    }

    @PostMapping("validateCreditCard")
    public ResponseEntity<AuthorizeDto> validateCreditCard(@RequestBody CreditCardInformationDto creditCardInformationDto) {
        return ResponseEntity.ok(depositAuthorizer.validateCard(creditCardInformationDto));
    }

    @PostMapping("add")
    public ResponseEntity<SettleDto> add(@RequestBody IbanAmountDto ibanAmountDto){
        log.info("iban info" + ibanAmountDto);
        return ResponseEntity.ok(settlePaymentAccount.settle(ibanAmountDto));
    }

    @PostMapping("deduct")
    public ResponseEntity<SettleDto> deduct(@RequestBody IbanAmountDto ibanAmountDto){
        log.info("iban info" + ibanAmountDto);

        return ResponseEntity.ok(settlePaymentAccount.settle(ibanAmountDto));
    }

    @PostMapping("reserveFunds")
    public ResponseEntity<String> reserveFunds(@RequestBody CreditCardInformationDto creditCardInformationDto){
        settlePaymentAccount.reserveFunds(creditCardInformationDto);
        return ResponseEntity.ok("Funds reserved");
    }




}
