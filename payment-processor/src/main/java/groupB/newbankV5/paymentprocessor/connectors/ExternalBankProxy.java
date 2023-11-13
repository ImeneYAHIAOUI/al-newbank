package groupB.newbankV5.paymentprocessor.connectors;

import groupB.newbankV5.paymentprocessor.connectors.dto.AuthorizeDto;
import groupB.newbankV5.paymentprocessor.connectors.dto.CcnResponseDto;
import groupB.newbankV5.paymentprocessor.connectors.dto.CreditCardInformationDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferDto;
import groupB.newbankV5.paymentprocessor.interfaces.IExternalBank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class ExternalBankProxy implements IExternalBank {

    private static final Logger log = Logger.getLogger(ExternalBankProxy.class.getName());

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${externalBank.host.baseurl:}")
    private String externalBankHostandPort;


    public AuthorizeDto authorizeTransfer(TransferDto transferDto) {

        log.info("\u001B[32mCalling external bank to authorize transfer\u001B[0m");
        try {
            return restTemplate.postForEntity(externalBankHostandPort + "/api/externalbank/authorize", transferDto, AuthorizeDto.class).getBody();
        } catch (Exception e) {
            log.warning("\u001B[31mError authorizing transfer: \u001B[0m" + e.getMessage());
            return null;
        }
    }

}
