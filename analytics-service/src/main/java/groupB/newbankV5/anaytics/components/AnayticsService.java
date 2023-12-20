package groupB.newbankV5.anaytics.components;

import groupB.newbankV5.anaytics.connectors.BusinessIntegratorProxy;
import groupB.newbankV5.anaytics.connectors.TransactionProxy;

import groupB.newbankV5.anaytics.entities.BankAccount;
import groupB.newbankV5.anaytics.entities.ClientAnalytics;
import groupB.newbankV5.anaytics.entities.Expense;
import groupB.newbankV5.anaytics.entities.Income;
import groupB.newbankV5.anaytics.entities.MerchantAnalytics;
import groupB.newbankV5.anaytics.entities.Transaction;
import groupB.newbankV5.anaytics.exceptions.MerchantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class AnayticsService {

    @Autowired
    private TransactionProxy transactionRepository;

    @Autowired
    private BusinessIntegratorProxy businessIntegratorProxy;


    public List<MerchantAnalytics> analyseMerchantBenifitsPerDay(
            String name    ) throws MerchantNotFoundException {
        BankAccount bankAccount = businessIntegratorProxy.get(name);
        String recipientIBAN= bankAccount.getIBAN();
        String recipientBIC= bankAccount.getBIC();
        List<Transaction> transactionList = List.of(transactionRepository.findAll());
        Map<LocalDate, List<Transaction>> transactionsByDay = transactionList.stream()
                .filter(transaction -> recipientIBAN.equals(transaction.getRecipient().getIBAN()))
                .filter(transaction -> recipientBIC.equals(transaction.getRecipient().getBIC()))
                .filter(Transaction::getExternal)
                .sorted(Comparator.comparing(Transaction::getTime))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTime(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        BigDecimal amountPreviousDay = BigDecimal.ZERO;
        List<MerchantAnalytics> amountReceivedPerDays = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Transaction>> entry : transactionsByDay.entrySet()) {
            MerchantAnalytics amountReceivedPerDay = new MerchantAnalytics();
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
            amountReceivedPerDay.setGrowth(dailyProfitVariation);
            amountReceivedPerDays.add(amountReceivedPerDay);
        }
        return amountReceivedPerDays;
    }

    public ClientAnalytics clientAnalytics( BankAccount bankAccount , int year, int month){
        ClientAnalytics clientAnalytics = new ClientAnalytics();
        String BIC = bankAccount.getBIC();
        String IBAN = bankAccount.getIBAN();
        YearMonth targetMonth = YearMonth.of(year, month);

        List<Transaction> transactions = List.of(transactionRepository.findAll());

        List<Income> incomeList = transactions.stream()
                .filter(transaction -> IBAN.equals(transaction.getRecipient().getIBAN()))
                .filter(transaction -> BIC.equals(transaction.getRecipient().getBIC()))
                .filter(transaction -> YearMonth.from(transaction.getTime()).equals(targetMonth))
                .sorted(Comparator.comparing(Transaction::getTime))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTime(),
                        TreeMap::new,
                        Collectors.toList()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Transaction> dailyTransactions = entry.getValue();
                    BigDecimal totalAmount = dailyTransactions.stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new Income(totalAmount, date);
                })
                .collect(Collectors.toList());

        List<Expense> expenseList = transactions.stream()
                .filter(transaction -> IBAN.equals(transaction.getSender().getIBAN()))
                .filter(transaction -> BIC.equals(transaction.getSender().getBIC()))
                .filter(transaction -> YearMonth.from(transaction.getTime()).equals(targetMonth))
                .sorted(Comparator.comparing(Transaction::getTime))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTime(),
                        TreeMap::new,
                        Collectors.toList()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Transaction> dailyTransactions = entry.getValue();
                    BigDecimal totalAmount = dailyTransactions.stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new Expense(totalAmount, date);
                })
                .collect(Collectors.toList());

        BigDecimal totalAmountIncome = incomeList.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmountExpense = expenseList.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        clientAnalytics.setExpense(expenseList);
        clientAnalytics.setIncome(incomeList);
        clientAnalytics.setMonthlyBalance(totalAmountIncome.subtract(totalAmountExpense));

        return clientAnalytics;

    }
}