package groupB.newbankV5.anaytics.components;

import groupB.newbankV5.anaytics.entities.AmountReceivedPerDay;
import groupB.newbankV5.anaytics.entities.Transaction;
import groupB.newbankV5.anaytics.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class AnayticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<AmountReceivedPerDay> analyseMerchantBenifitsPerDay(
            String recipientIBAN, String recipientBIC) {
        List<Transaction> transactionList = transactionRepository.findAll();
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
}