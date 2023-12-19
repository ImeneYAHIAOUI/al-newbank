package groupB.newbankV5.paymentgateway.connectors;


import groupB.newbankV5.paymentgateway.connectors.dto.CreditCardInformationDto;
import groupB.newbankV5.paymentgateway.interfaces.IMockBank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Component
public class MockBankProxy implements IMockBank {
    private static final Logger log = Logger.getLogger(MockBankProxy.class.getName());

    @Value("${externalBank.host.baseurl:}")
    private String MockBankHostandPort;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String reserveFunds(double amount, String cardNumber, String expiryDate, String cvv) {
        CreditCardInformationDto creditCard = new CreditCardInformationDto();
        creditCard.setAmount(amount);
        creditCard.setCardNumber(cardNumber);
        creditCard.setCvv(cvv);
        creditCard.setExpirationDate(expiryDate);
        log.info("Authorizing payment");
        return restTemplate.postForEntity( MockBankHostandPort + "/api/externalbank/reserveFunds", creditCard, String.class).getBody();
    }



}
