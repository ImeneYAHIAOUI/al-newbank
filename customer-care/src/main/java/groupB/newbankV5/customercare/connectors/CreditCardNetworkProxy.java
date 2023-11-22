package groupB.newbankV5.customercare.connectors;

import groupB.newbankV5.customercare.connectors.dto.CardGenerationRequestDto;
import groupB.newbankV5.customercare.connectors.dto.CreditCardDto;
import groupB.newbankV5.customercare.interfaces.ICreditCardNetwork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class CreditCardNetworkProxy implements ICreditCardNetwork {

    private static final Logger log = Logger.getLogger(CreditCardNetworkProxy.class.getName());


    @Value("${ccn.host.baseurl:}")
    private String ccnHostandPort;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public CreditCardDto getCreditCardDetails(CardGenerationRequestDto cardGenerationRequest) {
        log.info("Sending card creation request to Credit Card Network");
        return restTemplate.postForEntity(ccnHostandPort + "/api/virtualcard/generate", cardGenerationRequest, CreditCardDto.class).getBody();
    }


}
