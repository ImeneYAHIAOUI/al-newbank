package groupB.newbankV5.mockcreditcardnetwork.components;

import groupB.newbankV5.mockcreditcardnetwork.connectors.NewBankProxy;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardInformationDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.PaymentResponseDto;
import groupB.newbankV5.mockcreditcardnetwork.exceptions.ExpirationDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Pattern;
@Component
public class CreditCardAuthorizer {
    private static final Logger log = Logger.getLogger(CreditCardAuthorizer.class.getName());

    private static final String VISA_REGEX = "^4[0-9]{12}(?:[0-9]{3})?$";
    private static final String MASTERCARD_REGEX = "^5[1-5][0-9]{14}$";
    private static final String AMEX_REGEX = "^3[47][0-9]{13}$";
    private static final String NEWBANK_REGEX = "^6\\d{15}$";

    private final NewBankProxy newBankProxy;

    @Autowired
    public CreditCardAuthorizer(NewBankProxy newBankProxy) {
        this.newBankProxy = newBankProxy;
    }

    public PaymentResponseDto ValidateCreditCard(CreditCardInformationDto creditCardInformationDto) {
        log.info("Authorizing payment");
        String ccnumber = creditCardInformationDto.getCardNumber();
        if(isValidNewBank(ccnumber)) {
            return newBankProxy.checkNewBankCreditCard(creditCardInformationDto);
        }
        boolean validNumber =  isValidVisa(ccnumber) || isValidMastercard(ccnumber) || isValidAmex(ccnumber);
        log.info("Valid number: " + validNumber);
        boolean validCVV = isValidCVV(creditCardInformationDto.getCvv());
        log.info("Valid CVV: " + validCVV);
        boolean validDate = isValidDate(creditCardInformationDto.getExpirationDate());
        log.info("Valid date: " + validDate);
        boolean response = validNumber && validCVV && validDate;
        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setResponse(validNumber && validCVV && validDate);
        responseDto.setMessage();
        responseDto.setAuthToken();
        log.info(responseDto.toString());
        return responseDto;
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

    private boolean isValidNewBank(String cardNumber) {
        return Pattern.matches(NEWBANK_REGEX, cardNumber);
    }

    private boolean isValidCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    private boolean isValidDate(String date) throws ExpirationDateException{
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
