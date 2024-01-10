package groupB.newbankV5.paymentsettlement.components;

import groupB.newbankV5.paymentsettlement.connectors.ExternalBankProxy;
import groupB.newbankV5.paymentsettlement.connectors.TransactionProxy;
import groupB.newbankV5.paymentsettlement.connectors.dto.IbanAmountDto;
import groupB.newbankV5.paymentsettlement.connectors.dto.ReleaseFundsDto;
import groupB.newbankV5.paymentsettlement.connectors.dto.SettleDto;
import groupB.newbankV5.paymentsettlement.entities.Transaction;
import groupB.newbankV5.paymentsettlement.entities.TransactionStatus;
import groupB.newbankV5.paymentsettlement.interfaces.FeesCalculator;
import groupB.newbankV5.paymentsettlement.interfaces.IPaymentProcessor;
import groupB.newbankV5.paymentsettlement.interfaces.ICostumerCare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
public class SettlePayment {
    private static final Logger log = Logger.getLogger(SettlePayment.class.getName());
    private static final String NEWBANK_IBAN_REGEX = "^FR\\d{2}20523\\d+$";

    private final ExternalBankProxy externalBankProxy;
    private final ICostumerCare costumerCare;
    private final IPaymentProcessor paymentProcessorProxy;
    private final FeesCalculator feesCalculator;
    private final TransactionProxy transactionProxy;

    @Autowired
    public SettlePayment(ExternalBankProxy externalBankProxy, ICostumerCare costumerCare, IPaymentProcessor paymentProcessorProxy, FeesCalculator feesCalculator, TransactionProxy transactionProxy) {
        this.externalBankProxy = externalBankProxy;
        this.costumerCare = costumerCare;
        this.paymentProcessorProxy = paymentProcessorProxy;
        this.feesCalculator = feesCalculator;
        this.transactionProxy = transactionProxy;
    }



    public void deductFunds(List<ReleaseFundsDto> accounts) {
        log.info("accounts: " + Arrays.toString(accounts.toArray()));

        costumerCare.releaseFunds(accounts);
    }



    public void settlePayments() {
        Transaction[] transactions = transactionProxy.getTransactionsToSettle();
        log.info("\u001B[35mNumber of transactions to settle: " + transactions.length + "\u001B[0m");
        transactions = feesCalculator.calculateFees(transactions);
        List<ReleaseFundsDto> accounts = new ArrayList<>();
        for (Transaction transaction : transactions) {
            ReleaseFundsDto account;
            if(isNewBankAccount(transaction.getSender().getIBAN())) {
                if (isNewBankAccount(transaction.getRecipient().getIBAN())) {
                    account = new ReleaseFundsDto(BigDecimal.valueOf(Double.parseDouble(transaction.getAmount())), BigDecimal.valueOf(Double.parseDouble(transaction.getFees())),
                            transaction.getSender().getIBAN(), transaction.getRecipient().getIBAN());
                } else {
                    account = new ReleaseFundsDto(BigDecimal.valueOf(Double.parseDouble(transaction.getAmount())), BigDecimal.valueOf(Double.parseDouble(transaction.getFees())),
                            transaction.getSender().getIBAN());

                    SettleDto settleDto = externalBankProxy.addAmount(new IbanAmountDto(transaction.getRecipient().getIBAN(), Double.parseDouble(transaction.getAmount())));
                    if (settleDto == null) {
                        transaction.setStatus(TransactionStatus.FAILED);
                        continue;
                    }
                }
            }
            else {
                SettleDto deduceDto = externalBankProxy.deductAmount(new IbanAmountDto(transaction.getSender().getIBAN(), Double.parseDouble(transaction.getAmount())));
                if (deduceDto == null) {
                    transaction.setStatus(TransactionStatus.FAILED);
                    continue;
                }
                if (isNewBankAccount(transaction.getRecipient().getIBAN())) {
                    SettleDto settleDto = externalBankProxy.addAmount(new IbanAmountDto(transaction.getRecipient().getIBAN(), Double.parseDouble(transaction.getAmount())));
                    if (settleDto == null) {
                        transaction.setStatus(TransactionStatus.FAILED);
                        continue;
                    }
                }
                account = new ReleaseFundsDto(BigDecimal.valueOf(Double.parseDouble(transaction.getAmount())), BigDecimal.valueOf(Double.parseDouble(transaction.getFees())), transaction.getSender().getIBAN(), transaction.getRecipient().getIBAN());
            }
            accounts.add(account);
            transaction.setStatus(TransactionStatus.SETTLED);

        }
        deductFunds(accounts);
        transactionProxy.putTransactionsToSettle(transactions);
        paymentProcessorProxy.saveTransactions(transactions);


    }



    private boolean isNewBankAccount(String accountIban) {
        return accountIban.matches(NEWBANK_IBAN_REGEX);
    }


}

