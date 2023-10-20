package groupB.newbankV5.mockcreditcardnetwork.connectors;

import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CreditCardInformationDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class NewBankProxy {

    private static final Logger log = Logger.getLogger(NewBankProxy.class.getName());


    @Value("${newBank.host.baseurl:}")
    private String newBankHostandPort;

    private RestTemplate restTemplate = new RestTemplate();

    public PaymentResponseDto authorizePayment(CreditCardInformationDto paymentDetailsDTO) {
        log.info("Authorizing payment");

        return restTemplate.postForEntity(newBankHostandPort + "/api/payment/process", paymentDetailsDTO, PaymentResponseDto.class).getBody();
    }
}
