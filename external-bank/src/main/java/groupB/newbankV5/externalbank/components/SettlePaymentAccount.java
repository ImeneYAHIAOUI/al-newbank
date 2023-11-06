package groupB.newbankV5.externalbank.components;

import groupB.newbankV5.externalbank.components.dtos.CreditCardInformationDto;
import groupB.newbankV5.externalbank.controllers.dto.IbanAmountDto;
import groupB.newbankV5.externalbank.controllers.dto.SettleDto;
import org.springframework.stereotype.Component;

@Component
public class SettlePaymentAccount {

    private static final String REGEX_IBAN = "^[A-Z]{2}[0-9]*$";

    public SettleDto settle(IbanAmountDto transferDto) {
        String iban = transferDto.getIBAN();
        boolean valid = iban.matches(REGEX_IBAN) && iban.length() <= 36;;
        SettleDto authorizeDto = new SettleDto();
        authorizeDto.setSettled(valid);
        return authorizeDto;
    }

    public void reserveFunds(CreditCardInformationDto creditCardInformationDto) {
    }
}
