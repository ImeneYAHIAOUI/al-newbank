package groupB.newbankV5.paymentprocessor.connectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class CostumerCareProxy {
    private static final Logger log = Logger.getLogger(CreditCardNetworkProxy.class.getName());
    @Value("costumer.host.baseurl")
    private String costumerHostandPort;
    private RestTemplate restTemplate = new RestTemplate();

}
