package groupB.newbankV5.externalbank.components;

import groupB.newbankV5.externalbank.controllers.dto.AuthorizeDto;
import groupB.newbankV5.externalbank.controllers.dto.TransferDto;
import org.springframework.stereotype.Component;


@Component
public class DepositAuthorizer {

    private static final String REGEX_IBAN = "^[A-Z]{2}[0-9]*$";

    public AuthorizeDto authorize(TransferDto transferDto) {
        String iban = transferDto.getToAccountIBAN();
        boolean valid = iban.matches(REGEX_IBAN) && iban.length() <= 36;;
        AuthorizeDto authorizeDto = new AuthorizeDto();
        authorizeDto.setAuthorized(valid);
        return authorizeDto;
    }

}
