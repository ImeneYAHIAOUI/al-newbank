package groupB.newbankV5.anaytics.components;

import groupB.newbankV5.anaytics.entities.BankAccount;
import groupB.newbankV5.anaytics.entities.Transaction;
import org.springframework.stereotype.Component;
import java.util.TreeMap;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MainTest {
/*
    public static void main(String[] args) {

        Transaction t = new Transaction();
        BankAccount b = new BankAccount("123","xxx");
        t.setRecipient(b);
        t.setExternal(true);
        t.setFees(new BigDecimal(1));
        t.setExternal(true);
        t.setAmount(new BigDecimal(100));
        t.setTime(LocalDateTime.now().minusDays(1));

        Transaction t2 = new Transaction();
        BankAccount b2 = new BankAccount("123","xxx");
        t2.setRecipient(b2);
        t2.setFees(new BigDecimal(2));
        t2.setExternal(true);
        t2.setAmount(new BigDecimal(150));
        t2.setTime(LocalDateTime.now().minusDays(2));
        t2.setExternal(true);

        Transaction t3 = new Transaction();
        BankAccount b3 = new BankAccount("123","xxx");
        t3.setRecipient(b3);
        t3.setFees(new BigDecimal(2));
        t3.setExternal(true);
        t3.setAmount(new BigDecimal(200));
        t3.setTime(LocalDateTime.now().minusDays(2));
        System.out.println(groupTransactionsByDay(List.of(t,t2, t3),"123", "xxx"));
    }

    public static List<AmountReceivedPerDay> groupTransactionsByDay(List<Transaction> transactionList,
            String recipientIBAN, String recipientBIC) {

        Map<LocalDate, List<Transaction>> transactionsByDay = transactionList.stream()
                .filter(transaction -> recipientIBAN.equals(transaction.getRecipient().getIBAN()))
                .filter(transaction -> recipientBIC.equals(transaction.getRecipient().getBIC()))
                .filter(Transaction::getExternal)
                .sorted(Comparator.comparing(Transaction::getTime))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        BigDecimal amountPreviousDay = BigDecimal.ZERO;
        List<AmountReceivedPerDay> amountReceivedPerDays = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Transaction>> entry : transactionsByDay.entrySet()) {
            AmountReceivedPerDay amountReceivedPerDay = new AmountReceivedPerDay();
            amountReceivedPerDay.setDate(entry.getKey());
            BigDecimal fees = BigDecimal.ZERO;
            BigDecimal amount = BigDecimal.ZERO;
            for (Transaction transaction : entry.getValue()) {
                 fees = fees.add(transaction.getFees());
                amount = amount.add(transaction.getAmount());
            }
            amountReceivedPerDay.setTotalAmountReceived(amount);
            amountReceivedPerDay.setTotalFees(fees);
            BigDecimal dailyProfitVariation ;
            if(amountPreviousDay.equals(BigDecimal.ZERO)) {
                dailyProfitVariation = new BigDecimal(100);
            }else{
                dailyProfitVariation =
                        (((amount.subtract(fees)).subtract(amountPreviousDay)).divide(amountPreviousDay, 3,
                                RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
            }
            amountPreviousDay = amount.subtract(fees);
            amountReceivedPerDay.setPercentageProfitVariation(dailyProfitVariation);
            amountReceivedPerDays.add(amountReceivedPerDay);
        }
        return amountReceivedPerDays;
    }
*/
}
