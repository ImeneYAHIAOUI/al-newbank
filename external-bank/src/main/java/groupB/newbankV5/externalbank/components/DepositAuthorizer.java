package groupB.newbankV5.externalbank.components;

import groupB.newbankV5.externalbank.components.dtos.CreditCardInformationDto;
import groupB.newbankV5.externalbank.controllers.dto.AuthorizeDto;
import groupB.newbankV5.externalbank.controllers.dto.TransferDto;
import groupB.newbankV5.externalbank.exceptions.ExpirationDateException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


@Component
public class DepositAuthorizer {

    private static final String REGEX_IBAN = "^[A-Z]{2}[0-9]*$";
    private static final String VISA_REGEX = "^4[0-9]{12}(?:[0-9]{3})?$";
    private static final String MASTERCARD_REGEX = "^5[1-5][0-9]{14}$";
    private static final String AMEX_REGEX = "^3[47][0-9]{13}$";
    private static final String NEWBANK_REGEX = "^6\\d{15}$";

    public AuthorizeDto authorize(TransferDto transferDto) {
        String iban = transferDto.getToAccountIBAN();
        boolean valid = iban.matches(REGEX_IBAN) && iban.length() <= 36;;
        AuthorizeDto authorizeDto = new AuthorizeDto();
        authorizeDto.setAuthorized(valid);
        return authorizeDto;
    }

    public AuthorizeDto validateCard(CreditCardInformationDto creditCardInformationDto) {
        AuthorizeDto authorizeDto = new AuthorizeDto();
        String ccnumber = creditCardInformationDto.getCardNumber();
        boolean validNumber =  isValidVisa(ccnumber) || isValidMastercard(ccnumber) || isValidAmex(ccnumber);
        boolean validCVV = isValidCVV(creditCardInformationDto.getCvv());
        boolean validDate = isValidDate(creditCardInformationDto.getExpirationDate());
        boolean response = validNumber && validCVV && validDate;
        authorizeDto.setAuthorized(response);
        return authorizeDto;

    }

    private boolean isValidVisa(String cardNumber) {
        return Pattern.matches(VISA_REGEX, cardNumber);
    }

    private boolean isValidMastercard(String cardNumber) {
        return Pattern.matches(MASTERCARD_REGEX, cardNumber);
    }

    private boolean isValidAmex(String cardNumber) {
        return Pattern.matches(AMEX_REGEX, cardNumber);
    }
    private boolean isValidCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }
    private boolean isValidDate(String date) throws ExpirationDateException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

        dateFormat.setLenient(false);

        try {
            Date parsedDate = dateFormat.parse(date);

            Date currentDate = new Date();

            if (parsedDate.after(currentDate) || parsedDate.equals(currentDate)) {
                return true;
            }
        } catch (Exception e) {
            throw new ExpirationDateException(e.getMessage(),e.getCause());
        }

        return false;
    }

}
