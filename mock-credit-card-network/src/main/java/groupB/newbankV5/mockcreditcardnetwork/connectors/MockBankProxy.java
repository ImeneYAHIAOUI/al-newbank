package groupB.newbankV5.mockcreditcardnetwork.connectors;

import groupB.newbankV5.mockcreditcardnetwork.connectors.dtos.AuthorizeDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardInformationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class MockBankProxy {
    private static final Logger log = Logger.getLogger(MockBankProxy.class.getName());

    @Value("${mockbank.host.baseurl:}")
    private String mockBankHostandPort;
    private RestTemplate restTemplate = new RestTemplate();


    public AuthorizeDto checkMockBankCreditCard(CreditCardInformationDto creditCard ) {
        log.info("\u001B[32mSending credit card verification request to mock bank\u001B[0m");
        return restTemplate.postForEntity( mockBankHostandPort + "/api/externalbank/validateCreditCard", creditCard, AuthorizeDto.class).getBody();
    }
}
