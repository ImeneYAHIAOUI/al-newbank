package groupB.newbankV5.metrics.components;

import groupB.newbankV5.metrics.connectors.BusinessIntegratorProxy;

import groupB.newbankV5.metrics.controllers.dto.ApplicationDto;
import groupB.newbankV5.metrics.controllers.dto.MetricRequest;
import groupB.newbankV5.metrics.entities.*;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;
import groupB.newbankV5.metrics.interfaces.IMetricsService;
import groupB.newbankV5.metrics.repositories.RequestRepository;
import groupB.newbankV5.metrics.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class MetricsService implements IMetricsService {

    private static final List<String> SUPPORTED_RESOLUTIONS = List.of("5M","15M","30M","H", "D", "W", "M", "Y");

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BusinessIntegratorProxy businessIntegratorProxy;

    @Autowired
    private RequestRepository requestRepository;


    @Override
    public List<Metrics> sendMetrics(String token, MetricRequest request) throws InvalidTokenException, ApplicationNotFoundException {
        if (request.getResolution() == null || request.getResolution().isEmpty() || !SUPPORTED_RESOLUTIONS.contains(request.getResolution())) {
            throw new IllegalArgumentException("Invalid resolution");
        }

        if(request.getTimeRange() == null || request.getTimeRange().getFrom() == null || request.getTimeRange().getTo() == null || request.getTimeRange().getFrom().isAfter(request.getTimeRange().getTo())) {
            throw new IllegalArgumentException("Invalid time range");
        }

        ApplicationDto application = businessIntegratorProxy.validateToken(token);
        List<Transaction> transactions = transactionRepository.findByRecipientIBAN(application.getMerchant().getBankAccount().getIBAN()).stream().filter(transaction -> transaction.getTime().isAfter(request.getTimeRange().getTo()) && transaction.getTime().isBefore(request.getTimeRange().getFrom())).toList();
        List<Request> requests = requestRepository.findByApplication(application.getId()).stream().filter(request1 -> request1.getDateTime().isAfter(request.getTimeRange().getTo()) && request1.getDateTime().isBefore(request.getTimeRange().getFrom())).toList();
        return generateMetricsList(request, transactions,requests);

    }

    @Override
    public Request createRequest(Request request, String token) throws InvalidTokenException, ApplicationNotFoundException {
        ApplicationDto application = businessIntegratorProxy.validateToken(token);
        request.setApplication(application.getId());
        return requestRepository.save(request);
    }

    private List<Metrics> generateMetricsList(MetricRequest request, List<Transaction> timeRangetransactions, List<Request> timeRangeRequests) {
        List<Metrics> metricsList = new ArrayList<>();
        LocalDateTime current = request.getTimeRange().getFrom();
        LocalDateTime to = request.getTimeRange().getTo();
        int increment =  request.getResolution().equals("5M") ? 5 : request.getResolution().equals("15M") ? 15 : request.getResolution().equals("30M") ? 30 : 1;
        ChronoUnit chronoUnit = request.getResolution().equals("5M") || request.getResolution().equals("15M") || request.getResolution().equals("30M") ? ChronoUnit.MINUTES : request.getResolution().equals("H") ? ChronoUnit.HOURS : request.getResolution().equals("D") ? ChronoUnit.DAYS : request.getResolution().equals("W") ? ChronoUnit.WEEKS : request.getResolution().equals("M") ? ChronoUnit.MONTHS : ChronoUnit.YEARS;
        while (current.isBefore(to)) {
            Metrics metrics = new Metrics();
            metrics.setTimestamp(current);
            LocalDateTime finalCurrent = current;
            List<Transaction> transactions = timeRangetransactions.stream().filter(transaction -> transaction.getTime().isAfter(finalCurrent) && transaction.getTime().isBefore(finalCurrent.plus(increment, chronoUnit))).toList();
            List<Request> requests = timeRangeRequests.stream().filter(request1 -> request1.getDateTime().isAfter(finalCurrent) && request1.getDateTime().isBefore(finalCurrent.plus(increment, chronoUnit))).toList();
            if (request.getFilters() != null && !request.getFilters().isEmpty()) {
                for (Map.Entry<String, List<String>> filter : request.getFilters().entrySet()) {
                    switch (filter.getKey()) {
                        case "status" -> transactions = filterTransactionsByStatus(transactions, filter.getValue());
                        case "creditCardType" -> transactions = filterTransactionsByCreditCardType(transactions, filter.getValue());
                        default -> {
                        }
                    }
                }
            }
            if (request.getMetrics() == null || request.getMetrics().isEmpty()) {
                metrics.addValue("transactionCount", BigDecimal.valueOf(transactions.size()));
                metrics.addValue("totalRequestsCount", BigDecimal.valueOf(requests.size()));
            } else {
                for (String metric : request.getMetrics()) {
                    switch (metric) {
                        case "transactionCount" -> metrics.addValue("transactionCount", BigDecimal.valueOf(transactions.size()));
                        case "totalAmountSpent" -> metrics.addValue("totalAmountSpent", BigDecimal.valueOf(transactions.stream().map(Transaction::getAmount).mapToDouble(BigDecimal::doubleValue).sum()));
                        case "averageAmountSpent" -> metrics.addValue("averageAmountSpent", BigDecimal.valueOf(transactions.stream().map(Transaction::getAmount).mapToDouble(BigDecimal::doubleValue).average().orElse(0)));
                        case "TransactionSuccessRate" -> {
                            if (transactions.size() > 0) {
                                metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(transactions.stream().filter(transaction -> transaction.getStatus().equals(TransactionStatus.CONFIRMED)).count() / transactions.size()));
                            } else {
                                metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(0));
                            }
                        }
                        case "TransactionFailureRate" -> {
                            if (transactions.size() > 0) {
                                metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(transactions.stream().filter(transaction -> transaction.getStatus().equals(TransactionStatus.FAILED)).count() / transactions.size()));
                            } else {
                                metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(0));
                            }
                        }

                        case "totalFees" -> metrics.addValue("totalFees", BigDecimal.valueOf(transactions.stream().map(Transaction::getFees).mapToDouble(BigDecimal::doubleValue).sum()));
                        case "averageFees" -> metrics.addValue("averageFees", BigDecimal.valueOf(transactions.stream().map(Transaction::getFees).mapToDouble(BigDecimal::doubleValue).average().orElse(0)));
                        case "totalRequestsCount" -> metrics.addValue("totalRequestsCount", BigDecimal.valueOf(requests.size()));
                        case "successfulRequestsCount" -> metrics.addValue("successfulRequestsCount", BigDecimal.valueOf(requests.stream().filter(request1 -> request1.getStatus().equals("SUCCESS")).count()));
                        case "failedRequestsCount" -> metrics.addValue("failedRequestsCount", BigDecimal.valueOf(requests.stream().filter(request1 -> request1.getStatus().equals("FAILED")).count()));
                        case "averageRequestTime" -> metrics.addValue("averageRequestTime", BigDecimal.valueOf(requests.stream().map(Request::getTime).mapToDouble(BigDecimal::doubleValue).average().orElse(0)));
                        default -> {
                        }
                    }

                }
            }
            metricsList.add(metrics);
            current = current.plus(increment, chronoUnit);
        }
        return metricsList;
    }

    private List<Transaction> filterTransactionsByCreditCardType(List<Transaction> transactions, List<String> value) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (String creditCardType: value) {
            filteredTransactions.addAll(transactions.stream().filter(transaction -> transaction.getCreditCardType().toString().equals(creditCardType)).toList());
        }
        return filteredTransactions;
    }

    private List<Transaction> filterTransactionsByStatus(List<Transaction> transactions, List<String> value) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (String status: value) {
            filteredTransactions.addAll(transactions.stream().filter(transaction -> transaction.getStatus().toString().equals(status)).toList());
        }
        return filteredTransactions;
    }


}