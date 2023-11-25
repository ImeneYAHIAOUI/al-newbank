package groupB.newbankV5.metrics.connectors;

import groupB.newbankV5.metrics.entities.BankAccount;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;
import groupB.newbankV5.metrics.exceptions.MerchantNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class PaymentGatewayProxy  {
    private static final Logger log = Logger.getLogger(PaymentGatewayProxy.class.getName());
    @Value("${paymentGateway.host.baseurl:}")
    private String gatewayHostandPort;
    private final RestTemplate restTemplate = new RestTemplate();
    public long getConfirmedTransaction(long merchantId)throws ApplicationNotFoundException, InvalidTokenException{

            ResponseEntity<Long> responseEntity = restTemplate.getForEntity(gatewayHostandPort + "/api/gateway/transactions/confirmed/"+merchantId, Long.class);
            int statusCode = responseEntity.getStatusCodeValue();
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                throw new ApplicationNotFoundException("application not found");
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                throw new InvalidTokenException("token invalid");
            }
            return responseEntity.getBody();
    }
    public long getAuthorizedTransaction(long merchantId)throws ApplicationNotFoundException, InvalidTokenException {

            ResponseEntity<Long> responseEntity = restTemplate.getForEntity(gatewayHostandPort + "/api/gateway/transactions/authorized/"+merchantId, Long.class);
            int statusCode = responseEntity.getStatusCodeValue();
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                throw new ApplicationNotFoundException("application not found");
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                throw new InvalidTokenException("token invalid");
            }
            return responseEntity.getBody();
    }
}
