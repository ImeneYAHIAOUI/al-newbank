package groupB.newbankV5.anaytics.components;

import groupB.newbankV5.anaytics.entities.AmountReceivedPerDay;
import groupB.newbankV5.anaytics.entities.Transaction;
import groupB.newbankV5.anaytics.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class AnayticsService {

    @Autowired
    private TransactionRepository transactionRepository;
    public List<AmountReceivedPerDay> calculateTotalAmountReceivedPerDayForAccount(
            String recipientIBAN) {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<LocalDate, AmountReceivedPerDay> result = transactions.stream()
                .filter(transaction -> isTransactionForRecipient(transaction, recipientIBAN))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTime().toLocalDate(),
                        Collectors.reducing(new AmountReceivedPerDay(LocalDate.MIN, BigDecimal.ZERO, BigDecimal.ZERO),
                                transaction -> new AmountReceivedPerDay(transaction.getTime().toLocalDate(),
                                        transaction.getAmount().add(transaction.getFees()), transaction.getFees()),
                                (left, right) -> new AmountReceivedPerDay(left.getDate(),
                                        left.getTotalAmountReceived().add(right.getTotalAmountReceived()),
                                        left.getTotalFees().add(right.getTotalFees()))
                        )
                ));

        return result.values().stream().collect(Collectors.toList());
    }

    private boolean isTransactionForRecipient(Transaction transaction, String recipientIBAN) {
        return transaction.getExternal() && transaction.getAmount().compareTo(BigDecimal.ZERO) > 0
                && recipientIBAN.equals(transaction.getRecipient().getIBAN());
    }

}
