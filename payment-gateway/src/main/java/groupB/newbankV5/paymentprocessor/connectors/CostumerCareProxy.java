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
    private static final Logger log = Logger.getLogger(CreditCardNetworkProxy.class.getName());
    @Value("costumer.host.baseurl")
    private String costumerHostandPort;
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public AccountDto getAccountByIBAN(String accountNumber) {
        log.info("Getting balance for account number: " + accountNumber);
        return restTemplate.getForEntity(costumerHostandPort + "/api/costumer/search?iban=" + accountNumber, AccountDto.class).getBody();
    }

    @Override
    public AccountDto getAccountByCreditCard(String cardNumber, String expiryDate, String cvv) {
        log.info("Getting balance for credit card: " + cardNumber);
        return restTemplate.getForEntity(costumerHostandPort + "/api/costumer/search?number=" + cardNumber + "&date=" + expiryDate + "&cvv=" + cvv, AccountDto.class).getBody();
    }

    @Override
    public void updateBalance(long accountId, BigDecimal amount, String operation) {
        log.info("Updating balance for account number: " + accountId);
        UpdateFundsDto updateFundsDto = new UpdateFundsDto(amount, operation);
        restTemplate.put(costumerHostandPort + "/api/costumer/funds/" + accountId, updateFundsDto);
    }


}
