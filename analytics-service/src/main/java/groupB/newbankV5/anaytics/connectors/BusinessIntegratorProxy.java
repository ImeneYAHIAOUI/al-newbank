package groupB.newbankV5.anaytics.connectors;

import groupB.newbankV5.anaytics.entities.BankAccount;
import groupB.newbankV5.anaytics.exceptions.MerchantNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class BusinessIntegratorProxy  {
    private static final Logger log = Logger.getLogger(BusinessIntegratorProxy.class.getName());
    @Value("${businessIntegrator.host.baseurl:}")
    private String integratorHostandPort;
    private final RestTemplate restTemplate = new RestTemplate();

    public BankAccount get(String name) throws MerchantNotFoundException {
        try{
            ResponseEntity<BankAccount> responseEntity =
                    restTemplate.getForEntity(integratorHostandPort + "/api/integration/merchants?name=" + name,
                            BankAccount.class);
            return responseEntity.getBody();

        }catch (Exception e){
            throw new MerchantNotFoundException("merchant not found");

        }
    }


}
