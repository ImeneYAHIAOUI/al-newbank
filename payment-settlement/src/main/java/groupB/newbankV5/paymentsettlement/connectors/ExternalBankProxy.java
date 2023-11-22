package groupB.newbankV5.paymentsettlement.connectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupB.newbankV5.paymentsettlement.connectors.dto.SettleDto;
import groupB.newbankV5.paymentsettlement.connectors.dto.IbanAmountDto;
import groupB.newbankV5.paymentsettlement.interfaces.IExternalBank;
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



    public SettleDto addAmount(IbanAmountDto transferDto) {
        log.info("\u001B[35mCalling external bank to add amount\u001B[0m");
        try {
            return restTemplate.postForEntity(externalBankHostandPort + "/api/externalbank/add", transferDto,
                    SettleDto.class).getBody();
        } catch (Exception e) {
            log.warning("Error authorizing transfer: " + e.getMessage());
            return null;
        }
    }

    public SettleDto deductAmount(IbanAmountDto transferDto) {
        log.info("Calling external bank to deduct amount "+ transferDto);

        try {

            return restTemplate.postForEntity(externalBankHostandPort + "/api/externalbank/deduct", transferDto,
                    SettleDto.class).getBody();
        } catch (Exception e) {
            log.warning("Error authorizing transfer: " + e.getMessage());
            return null;
        }
    }

}
