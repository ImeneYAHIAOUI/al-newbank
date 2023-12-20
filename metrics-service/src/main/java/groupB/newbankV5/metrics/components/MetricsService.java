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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Component
public class MetricsService implements IMetricsService {

    private static final Logger log = LoggerFactory.getLogger(MetricsService.class);

    private static final List<String> SUPPORTED_RESOLUTIONS = List.of("5M","15M","30M","H", "D", "W", "M", "Y");

    private static final Pattern PERIOD_PATTERN =   Pattern.compile("(?i)[Ll]\\d+(mi|h|d|w|m|y)", Pattern.CASE_INSENSITIVE);



    private final TransactionRepository transactionRepository;
    private final BusinessIntegratorProxy businessIntegratorProxy;

    private final RequestRepository requestRepository;

    @Autowired
    public MetricsService(TransactionRepository transactionRepository, BusinessIntegratorProxy businessIntegratorProxy, RequestRepository requestRepository) {
        this.transactionRepository = transactionRepository;
        this.businessIntegratorProxy = businessIntegratorProxy;
        this.requestRepository = requestRepository;
    }


    @Override
    public List<Metrics> sendMetrics(String token, MetricRequest request) throws InvalidTokenException, ApplicationNotFoundException {
        if (request.getResolution() == null || request.getResolution().isEmpty() || !SUPPORTED_RESOLUTIONS.contains(request.getResolution())) {
            throw new IllegalArgumentException("Invalid resolution");
        }

        if (request.getPeriod() == null && request.getTimeRange() == null) {
            throw new IllegalArgumentException("period or time range must be provided");
        }

        if(request.getPeriod() != null && !PERIOD_PATTERN.matcher(request.getPeriod()).matches()) {
            throw new IllegalArgumentException("Invalid period");
        }
        if(request.getTimeRange() != null && ( request.getTimeRange().getFrom() == null || request.getTimeRange().getTo() == null || request.getTimeRange().getFrom().isAfter(request.getTimeRange().getTo()))) {
            throw new IllegalArgumentException("Invalid time range");
        }

        LocalDateTime from;
        LocalDateTime to;

        if (request.getPeriod() != null) {
            String period = request.getPeriod();
            int amount =period.length() == 3 ? Integer.parseInt(period.substring(1, period.length() - 1)) : Integer.parseInt(period.substring(1, period.length() - 2));
            ChronoUnit chronoUnit = period.endsWith("mi") || period.endsWith("MI") || period.endsWith("Mi") || period.endsWith("mI") ? ChronoUnit.MINUTES : period.endsWith("h") || period.endsWith("H") ? ChronoUnit.HOURS : period.endsWith("d")  || period.endsWith("D") ? ChronoUnit.DAYS : period.endsWith("w")  || period.endsWith("W") ? ChronoUnit.WEEKS : period.endsWith("m") || period.endsWith("M") ? ChronoUnit.MONTHS : ChronoUnit.YEARS;
            to = LocalDateTime.now();
            from = to.minus(amount, chronoUnit);

        } else {
            to = request.getTimeRange().getTo();
            from = request.getTimeRange().getFrom();
        }

        long toInMillis = to.toInstant(ZoneOffset.UTC).toEpochMilli();
        long fromInMillis = from.toInstant(ZoneOffset.UTC).toEpochMilli();


        ApplicationDto application = businessIntegratorProxy.validateToken(token);



        List<Transaction> transactions = transactionRepository.findAll().stream()
                .map(transactionEnvelop -> transactionEnvelop.getPayload().getTransaction())
                .filter(transaction -> transaction.getTime() > fromInMillis && transaction.getTime() < toInMillis).toList();
        List<Request> requests = requestRepository.findAll().stream()
                .filter(request1 -> request1.getDateTime().isAfter(from) && request1.getDateTime().isBefore(to)).toList();
        log.info("from: " + from);
        log.info("to: " + to);
        log.info("from in millis: " + fromInMillis);
        log.info("to in millis: " + toInMillis);
        log.info("Transactions: " + transactions);
        log.info("Requests: " + requests);
        log.info("total requests: " + requestRepository.findAll());
        log.info("total transactions: " + transactionRepository.findAll());
        return generateMetricsList(from, to, request,transactions,requests);

    }

    @Override
    public Request createRequest(Request request, String token) throws InvalidTokenException, ApplicationNotFoundException {
        ApplicationDto application = businessIntegratorProxy.validateToken(token);
        request.setApplication(application.getId());
        return requestRepository.save(request);
    }

    private List<Metrics> generateMetricsList(LocalDateTime from, LocalDateTime to, MetricRequest request, List<Transaction> timeRangeTransactions, List<Request> timeRangeRequests) {
        List<Metrics> metricsList = new ArrayList<>();
        LocalDateTime current = from;
        int increment =  request.getResolution().equals("5M") ? 5 : request.getResolution().equals("15M") ? 15 : request.getResolution().equals("30M") ? 30 : 1;
        ChronoUnit chronoUnit = request.getResolution().equals("5M") || request.getResolution().equals("15M") || request.getResolution().equals("30M") ? ChronoUnit.MINUTES : request.getResolution().equals("H") ? ChronoUnit.HOURS : request.getResolution().equals("D") ? ChronoUnit.DAYS : request.getResolution().equals("W") ? ChronoUnit.WEEKS : request.getResolution().equals("M") ? ChronoUnit.MONTHS : ChronoUnit.YEARS;
        long toInMillis = to.toInstant(ZoneOffset.UTC).toEpochMilli();
        while (current.isBefore(to)) {
            Metrics metrics = new Metrics();
            metrics.setTimestamp(current);
            LocalDateTime finalCurrent = current;
            long fromInMillis = current.toInstant(ZoneOffset.UTC).toEpochMilli();
            List<Transaction> transactions = timeRangeTransactions.stream().filter(transaction -> transaction.getTime() > fromInMillis && transaction.getTime() < toInMillis).toList();
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
                metrics.addValue("averageAmountSpent", BigDecimal.valueOf(transactions.stream().map(Transaction::getAmount).mapToDouble(Double::parseDouble).average().orElse(0)));
                if (transactions.size() > 0) {
                    metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(transactions.stream().filter(transaction -> transaction.getStatus().equals(TransactionStatus.CONFIRMED.getValue())).count() / transactions.size()));
                } else {
                    metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(0));
                }
                if (transactions.size() > 0) {
                    metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(transactions.stream().filter(transaction -> transaction.getStatus().equals(TransactionStatus.FAILED.getValue())).count() / transactions.size()));
                } else {
                    metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(0));
                }
                metrics.addValue("totalFees", BigDecimal.valueOf(transactions.stream().map(Transaction::getFees).mapToDouble(Double::parseDouble).sum()));
                metrics.addValue("averageFees", BigDecimal.valueOf(transactions.stream().map(Transaction::getFees).mapToDouble(Double::parseDouble).average().orElse(0)));
                metrics.addValue("successfulRequestsCount", BigDecimal.valueOf(requests.stream().filter(request1 -> request1.getStatus().equals("SUCCESS")).count()));
                metrics.addValue("failedRequestsCount", BigDecimal.valueOf(requests.stream().filter(request1 -> request1.getStatus().equals("FAILED")).count()));
                metrics.addValue("averageRequestTime", BigDecimal.valueOf(requests.stream().map(Request::getTime).mapToDouble(BigDecimal::doubleValue).average().orElse(0)));


            } else {
                for (String metric : request.getMetrics()) {
                    switch (metric) {
                        case "transactionCount" -> metrics.addValue("transactionCount", BigDecimal.valueOf(transactions.size()));
                        case "totalAmountSpent" -> metrics.addValue("totalAmountSpent", BigDecimal.valueOf(transactions.stream().map(Transaction::getAmount).mapToDouble(Double::parseDouble).sum()));
                        case "averageAmountSpent" -> metrics.addValue("averageAmountSpent", BigDecimal.valueOf(transactions.stream().map(Transaction::getAmount).mapToDouble(Double::parseDouble).average().orElse(0)));
                        case "TransactionSuccessRate" -> {
                            if (transactions.size() > 0) {
                                metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(transactions.stream().filter(transaction -> transaction.getStatus().equals(TransactionStatus.CONFIRMED.getValue())).count() / transactions.size()));
                            } else {
                                metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(0));
                            }
                        }
                        case "TransactionFailureRate" -> {
                            if (transactions.size() > 0) {
                                metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(transactions.stream().filter(transaction -> transaction.getStatus().equals(TransactionStatus.FAILED.getValue())).count() / transactions.size()));
                            } else {
                                metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(0));
                            }
                        }

                        case "totalFees" -> metrics.addValue("totalFees", BigDecimal.valueOf(transactions.stream().map(Transaction::getFees).mapToDouble(Double::parseDouble).sum()));
                        case "averageFees" -> metrics.addValue("averageFees", BigDecimal.valueOf(transactions.stream().map(Transaction::getFees).mapToDouble(Double::parseDouble).average().orElse(0)));
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
            filteredTransactions.addAll(transactions.stream().filter(transaction -> transaction.getCreditCardType().equals(creditCardType)).toList());
        }
        return filteredTransactions;
    }

    private List<Transaction> filterTransactionsByStatus(List<Transaction> transactions, List<String> value) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (String status: value) {
            filteredTransactions.addAll(transactions.stream().filter(transaction -> transaction.getStatus().equals(status)).toList());
        }
        return filteredTransactions;
    }


}