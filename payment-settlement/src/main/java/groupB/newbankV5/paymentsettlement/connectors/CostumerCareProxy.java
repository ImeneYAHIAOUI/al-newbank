package groupB.newbankV5.paymentsettlement.connectors;

import groupB.newbankV5.paymentsettlement.connectors.dto.AccountDto;
import groupB.newbankV5.paymentsettlement.connectors.dto.ReserveFundsDto;
import groupB.newbankV5.paymentsettlement.connectors.dto.UpdateFundsDto;
import groupB.newbankV5.paymentsettlement.interfaces.ICostumerCare;
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
            AccountDto c = restTemplate.getForEntity(costumerHostandPort + "/api/costumer/search?iban=" + accountNumber,
                    AccountDto.class).getBody();
            log.info(" "+c);
            return c;
        } catch (Exception e) {
            log.warning("Error getting balance for account number: " + e.getMessage());
            return null;
        }
    }


    @Override
    public void updateBalance(long accountId, BigDecimal amount, String operation) {
        log.info("Updating balance for account number: " + accountId);
        UpdateFundsDto updateFundsDto = new UpdateFundsDto(amount, operation);
        restTemplate.put(costumerHostandPort + "/api/costumer/" + accountId+ "/funds", updateFundsDto);
    }

    @Override
    public void releaseFunds(long accountId, BigDecimal amount) {
        log.info("Releasing funds for account number: " + accountId);
        ReserveFundsDto releaseFundsDto = new ReserveFundsDto(amount);
        restTemplate.put(costumerHostandPort + "/api/costumer/" + accountId+ "/releasefunds", releaseFundsDto);
    }


}
