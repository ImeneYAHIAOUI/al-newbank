package groupB.newbankV5.mockcreditcardnetwork.components;

import groupB.newbankV5.mockcreditcardnetwork.connectors.MockBankProxy;
import groupB.newbankV5.mockcreditcardnetwork.connectors.NewBankProxy;
import groupB.newbankV5.mockcreditcardnetwork.connectors.dtos.AuthorizeDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardInformationDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardCheckResponseDto;
import groupB.newbankV5.mockcreditcardnetwork.exceptions.ExpirationDateException;
import groupB.newbankV5.mockcreditcardnetwork.exceptions.InvalidCardInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;
@Component
public class CreditCardAuthorizer {
    private static final Logger log = Logger.getLogger(CreditCardAuthorizer.class.getName());
    private static final String NEWBANK_REGEX = "^6\\d{15}$";
    private final NewBankProxy newBankProxy;
    private final MockBankProxy mockBankProxy;

    @Autowired
    public CreditCardAuthorizer(NewBankProxy newBankProxy, MockBankProxy mockBankProxy) {
        this.newBankProxy = newBankProxy;
        this.mockBankProxy = mockBankProxy;
    }

    public CreditCardCheckResponseDto ValidateCreditCard(CreditCardInformationDto creditCardInformationDto) throws InvalidCardInformation {
        String ccnumber = creditCardInformationDto.getCardNumber();
        if(isValidNewBank(ccnumber)) {
            CreditCardCheckResponseDto resp = newBankProxy.checkNewBankCreditCard(creditCardInformationDto);
            resp.setBankName("NewBank");
            String response = resp.getResponse() ? "Valid" : "Invalid";
            log.info("\u001B[32m" + response + " credit card from NewBank\u001B[0m");
            return resp;
        }
        AuthorizeDto response = mockBankProxy.checkMockBankCreditCard(creditCardInformationDto);
        if (response.isAuthorized()) {
            CreditCardCheckResponseDto responseDto = new CreditCardCheckResponseDto();
            Random random = new Random();
            int randomValue = random.nextInt(2);
            String cardType = (randomValue == 0) ? "DEBIT" : "CREDIT";
            responseDto.setResponse(true);
            responseDto.setMessage();
            responseDto.setAuthToken();
            responseDto.setAccountIBAN("FR2052300000000000000000000");
            responseDto.setAccountBIC("EXTERNAL");
            responseDto.setCardType(cardType);
            responseDto.setBankName("MockBank");
            log.info("\u001B[32mValid credit card from MockBank\u001B[0m");
            return responseDto;
        }
        else throw new InvalidCardInformation("Invalid credit card information");
    }


    private boolean isValidNewBank(String cardNumber) {
        return Pattern.matches(NEWBANK_REGEX, cardNumber);
    }


}
