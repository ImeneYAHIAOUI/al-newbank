package groupB.newbankV5.customercare.interfaces;

import groupB.newbankV5.customercare.controllers.dto.ReleaseFundsDto;
import groupB.newbankV5.customercare.entities.Account;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;

public interface FundsHandler {

    Account updateFunds(Account account, BigDecimal amount, String operation);
    Account addReservedFunds(Account account, BigDecimal amount, String cardNumber, String expirationDate, String cvv);

    Account deduceFromWeeklyLimit(Account account, BigDecimal amount);

    Account releaseReservedFunds(ReleaseFundsDto amount);

    @Scheduled(cron = "0 0 1 * *")
    void resetCreditCardLimit();

    @Scheduled(cron = "0 0 0 * * MON")
    void resetWeeklyPaymentLimit();
}
