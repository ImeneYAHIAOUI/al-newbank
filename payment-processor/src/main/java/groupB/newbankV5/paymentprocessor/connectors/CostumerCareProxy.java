package groupB.newbankV5.paymentprocessor.connectors;

import groupB.newbankV5.paymentprocessor.connectors.dto.*;
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
        try{
            return restTemplate.getForEntity(costumerHostandPort + "/api/costumer/search?iban=" + accountNumber, AccountDto.class).getBody();
        } catch (Exception e) {
            log.warning("\u001B[31mError getting account for IBAN: \u001B[0m" + e.getMessage());
            return null;
        }
    }

    @Override
    public AccountDto getAccountByCreditCard(String cardNumber, String expiryDate, String cvv) {
        try{
            return restTemplate.getForEntity(costumerHostandPort + "/api/costumer/search?number=" + cardNumber + "&date=" + expiryDate + "&cvv=" + cvv, AccountDto.class).getBody();

        } catch (Exception e) {
            log.warning("\u001B[31mError getting account for credit card: \u001B[0m" + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateBalance(long accountId, BigDecimal amount, String operation) {
        UpdateFundsDto updateFundsDto = new UpdateFundsDto(amount, operation);
        restTemplate.put(costumerHostandPort + "/api/costumer/"+accountId+"/funds" , updateFundsDto);
    }

    @Override
    public void reserveFunds(BigDecimal amount, String cardNumber, String expirationDate, String cvv) {
        ReserveFundsDto reserveFundsDto = new ReserveFundsDto(amount, cardNumber, expirationDate, cvv);
        restTemplate.put(costumerHostandPort + "/api/costumer/reservedfunds" , reserveFundsDto);
    }

    @Override
    public void deduceWeeklyLimit(long accountId, BigDecimal amount) {
        UpdateWeeklyLimitDto updateWeeklyLimitDto = new UpdateWeeklyLimitDto(amount);
        restTemplate.put(costumerHostandPort + "/api/costumer/"+accountId+"/deduceweeklylimit" , updateWeeklyLimitDto);
    }




}
