package groupB.newbankV5.paymentsettlement.components;

import groupB.newbankV5.paymentsettlement.connectors.ExternalBankProxy;
import groupB.newbankV5.paymentsettlement.connectors.dto.AccountDto;
import groupB.newbankV5.paymentsettlement.connectors.dto.IbanAmountDto;
import groupB.newbankV5.paymentsettlement.entities.Transaction;
import groupB.newbankV5.paymentsettlement.entities.TransactionStatus;
import groupB.newbankV5.paymentsettlement.interfaces.ICostumerCare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Component
public class SettlePayment {
    private static final Logger log = Logger.getLogger(SettlePayment.class.getName());
    private static final String NEWBANK_IBAN_REGEX = "^FR\\d{2}20523\\d+$";

    private final ExternalBankProxy externalBankProxy;
    private final ICostumerCare costumerCare;

    @Autowired
    public SettlePayment(ExternalBankProxy externalBankProxy, ICostumerCare costumerCare) {
        this.externalBankProxy = externalBankProxy;
        this.costumerCare = costumerCare;
    }

    public boolean hasSufficientFunds(AccountDto accountDto, BigDecimal amount) {
        BigDecimal balance =  accountDto.getReservedBalance();
        return balance.compareTo(amount) >= 0;
    }

    public void deductFunds(long accountId, BigDecimal amount) {
        log.info("Deducting funds");
        costumerCare.releaseFunds(accountId, amount);
    }

    public void depositFund(long accountId, BigDecimal amount) {
        log.info("Depositing funds");
        costumerCare.updateBalance(accountId, amount, "deposit");
    }

    public void settlePayment(Transaction transaction) {
        String from_IBAN = transaction.getSender().getIBAN();
        String to_IBAN = transaction.getRecipient().getIBAN();
        BigDecimal amount = transaction.getAmount();
        if (from_IBAN == null || to_IBAN == null) {
            transaction.setStatus(TransactionStatus.FAILED);
            return;
        }
        if (isNewBankAccount(from_IBAN)) {
            AccountDto sourceAccount = costumerCare.getAccountByIBAN(from_IBAN);
            if (hasSufficientFunds(sourceAccount, transaction.getAmount())) {
                handlePayment(sourceAccount, to_IBAN, amount, from_IBAN, transaction);
                return;
            }
        } else if (externalBankProxy.deductAmount(new IbanAmountDto(from_IBAN, amount)).isSettled()) {
            if (isNewBankAccount(to_IBAN)) {
                AccountDto destinationAccount = costumerCare.getAccountByIBAN(to_IBAN);
                depositFund(destinationAccount.getId(), amount.subtract(transaction.getFees()));
                transaction.setStatus(TransactionStatus.SETTLED);
                return;
            } else if (externalBankProxy.addAmount(new IbanAmountDto(to_IBAN, amount)).isSettled()) {
                transaction.setStatus(TransactionStatus.SETTLED);
                return;
            }
        }

        transaction.setStatus(TransactionStatus.FAILED);
    }

    private void handlePayment(AccountDto sourceAccount, String to_IBAN, BigDecimal amount, String from_IBAN,
            Transaction transaction) {
        if (isNewBankAccount(to_IBAN)) {
            AccountDto destinationAccount = costumerCare.getAccountByIBAN(to_IBAN);

            deductFunds(sourceAccount.getId(), amount);
            depositFund(destinationAccount.getId(), amount.subtract(transaction.getFees()));
            transaction.setStatus(TransactionStatus.SETTLED);
            return;
        } else if (externalBankProxy.addAmount(new IbanAmountDto(from_IBAN, amount )).isSettled()) {
            deductFunds(sourceAccount.getId(), amount);
            transaction.setStatus(TransactionStatus.SETTLED);
            return;
        }
        transaction.setStatus(TransactionStatus.FAILED);

    }



    private boolean isNewBankAccount(String accountIban) {
        return accountIban.matches(NEWBANK_IBAN_REGEX);
    }


}

