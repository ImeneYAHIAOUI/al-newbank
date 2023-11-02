package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.connectors.TransactionProxy;
import groupB.newbankV5.paymentprocessor.entities.Transaction;
import groupB.newbankV5.paymentprocessor.entities.TransactionStatus;
import groupB.newbankV5.paymentprocessor.repositories.TransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionHandler {

    private final TransactionRepository transactionRepository;

    public TransactionHandler(TransactionProxy transactionProxy, TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactionsWeekly(String iban) {
        LocalDateTime time = LocalDate.now().minusDays(6).atStartOfDay();
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getSender().getIBAN().equals(iban))
                .filter(transaction -> transaction.getTime() != null)
                .filter(transaction -> !transaction.getStatus().equals(TransactionStatus.FAILED))
                .filter(transaction -> transaction.getTime().isAfter(time))
                .collect(Collectors.toList());
    }

    public void addTransaction(Transaction newTransaction) {
        transactionRepository.save(newTransaction);
    }

    @Scheduled(cron="0 0 * * * *")
    public void cleanLastWeekTransactions(){
        LocalDateTime time = LocalDate.now().minusDays(6).atStartOfDay();
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTime() != null)
                .filter(transaction -> transaction.getTime().isBefore(time))
                .collect(Collectors.toList());
        transactionRepository.deleteAll(transactions);
    }


}