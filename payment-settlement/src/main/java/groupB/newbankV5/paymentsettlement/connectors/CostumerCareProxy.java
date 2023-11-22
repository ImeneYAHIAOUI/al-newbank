package groupB.newbankV5.paymentsettlement.connectors;


import groupB.newbankV5.paymentsettlement.connectors.dto.ReleaseFundsDto;
import groupB.newbankV5.paymentsettlement.entities.Transaction;
import groupB.newbankV5.paymentsettlement.interfaces.ICostumerCare;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Component
public class CostumerCareProxy implements ICostumerCare {
    private static final Logger log = Logger.getLogger(CostumerCareProxy.class.getName());

    @Value("${costumer.host.baseurl:}")

    private String costumerHostandPort;
    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public void releaseFunds(List<ReleaseFundsDto> accounts) {
        log.info("\u001B[35mSending release funds request to costumer care\u001B[0m");
         restTemplate.postForEntity(
                costumerHostandPort + "/api/costumer/batchReleaseFunds",
                accounts,
                Void.class).getBody();

    }


}
