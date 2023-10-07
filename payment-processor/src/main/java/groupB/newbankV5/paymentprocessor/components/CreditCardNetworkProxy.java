package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.components.dto.CcnResponseDto;
import groupB.newbankV5.paymentprocessor.components.dto.CreditCardInformationDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CreditCardNetworkProxy {

    @Value("${ccn.host.baseurl:}")
    private String ccnHostandPort;

    private RestTemplate restTemplate = new RestTemplate();

    public CcnResponseDto authorizePayment(PaymentDetailsDTO paymentDetailsDTO) {

        CreditCardInformationDto creditCardInformationDto = new CreditCardInformationDto();
        creditCardInformationDto.setCardNumber(paymentDetailsDTO.getCardNumber());
        creditCardInformationDto.setExpirationDate(paymentDetailsDTO.getExpirationDate());
        creditCardInformationDto.setCvv(paymentDetailsDTO.getCvv());

        return restTemplate.postForEntity(ccnHostandPort + "/api/payment/authorize", creditCardInformationDto, CcnResponseDto.class).getBody();
    }

}
