package groupB.newbankV5.paymentprocessor.connectors;

import groupB.newbankV5.paymentprocessor.connectors.dto.AccountDto;
import groupB.newbankV5.paymentprocessor.connectors.dto.UpdateFundsDto;
import groupB.newbankV5.paymentprocessor.interfaces.ICostumerCare;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Component
public class CostumerCareProxy implements ICostumerCare {
    private static final Logger log = Logger.getLogger(CostumerCareProxy.class.getName());

    @Value("${costumer.host.baseurl:}")

    private String costumerHostandPort;
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public AccountDto getAccountByIBAN(String accountNumber) {
        log.info("Getting balance for account number: " + accountNumber);
        try{
            log.info("port and host "+ costumerHostandPort);
            return restTemplate.getForEntity(costumerHostandPort + "/api/costumer/search?iban=" + accountNumber, AccountDto.class).getBody();
        } catch (Exception e) {
            log.warning("Error getting balance for account number: " + e.getMessage());
            return null;
        }
    }

    @Override
    public AccountDto getAccountByCreditCard(String cardNumber, String expiryDate, String cvv) {
        log.info("Getting balance for credit card: " + "/api/costumer/search?number=" + cardNumber + "&date=" + expiryDate + "&cvv=" + cvv);
        try{
            return restTemplate.getForEntity(costumerHostandPort + "/api/costumer/search?number=" + cardNumber + "&date=" + expiryDate + "&cvv=" + cvv, AccountDto.class).getBody();

        } catch (Exception e) {
            log.warning("Error getting account for credit card: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateBalance(long accountId, BigDecimal amount, String operation) {
        log.info("Updating balance for account number: " + accountId);
        UpdateFundsDto updateFundsDto = new UpdateFundsDto(amount, operation);
        restTemplate.put(costumerHostandPort + "/api/costumer"+accountId+"/funds" , updateFundsDto);


    }


}
