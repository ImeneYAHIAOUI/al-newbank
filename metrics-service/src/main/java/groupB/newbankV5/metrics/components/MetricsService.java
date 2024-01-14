package groupB.newbankV5.metrics.components;

import groupB.newbankV5.metrics.connectors.BusinessIntegratorProxy;

import groupB.newbankV5.metrics.controllers.dto.ApplicationDto;
import groupB.newbankV5.metrics.controllers.dto.MetricRequest;
import groupB.newbankV5.metrics.entities.*;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;
import groupB.newbankV5.metrics.interfaces.IMetricsService;
import groupB.newbankV5.metrics.repositories.RedisTransactionRepository;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.groupingBy;


@Component
public class MetricsService implements IMetricsService {

    private static final Logger log = LoggerFactory.getLogger(MetricsService.class);

    private static final List<String> SUPPORTED_RESOLUTIONS = List.of("5M","15M","30M","H", "D", "W", "M", "Y");

    private static final Pattern PERIOD_PATTERN =   Pattern.compile("(?i)[Ll]\\d+(mi|h|d|w|m|y)", Pattern.CASE_INSENSITIVE);



    private final TransactionRepository transactionRepository;

    private final RedisTransactionRepository redisTransactionRepository;
    private final BusinessIntegratorProxy businessIntegratorProxy;

    private final RequestRepository requestRepository;

    @Autowired
    public MetricsService(TransactionRepository transactionRepository, RedisTransactionRepository redisTransactionRepository, BusinessIntegratorProxy businessIntegratorProxy, RequestRepository requestRepository) {
        this.transactionRepository = transactionRepository;
        this.redisTransactionRepository = redisTransactionRepository;
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


        List<Transaction> transactions = new ArrayList<>(transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getApplicationId() == application.getId())
                .filter(transaction -> transaction.getTime() > fromInMillis && transaction.getTime() < toInMillis).toList());

        List<Transaction> redisTransactions = redisTransactionRepository.findAll().stream()
                .map(RedisTransaction::getTransactionId)
                .filter(transactionId -> transactionId.matches(".*" + application.getId() + ".*"))
                .map(this::redisTransactionToTransaction).filter(Optional::isPresent).map(Optional::get)
                .filter(transaction -> transaction.getTime() > fromInMillis && transaction.getTime() < toInMillis)
                .toList();

        transactions.addAll(redisTransactions);

        List<Request> requests = requestRepository.findAll().stream()
                .filter(request1 -> request1.getApplication().equals(application.getId()))
                .filter(request1 -> request1.getDateTime().isAfter(from) && request1.getDateTime().isBefore(to)).toList();





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
        long incrementInMillis = chronoUnit.getDuration().toMillis() * increment;
        while (current.isBefore(to)) {
            Metrics metrics = new Metrics();
            metrics.setTimestamp(current);
            LocalDateTime finalCurrent = current;
            long fromInMillis = current.toInstant(ZoneOffset.UTC).toEpochMilli();
            List<Transaction> transactions = timeRangeTransactions.stream().filter(transaction -> transaction.getTime() > fromInMillis && transaction.getTime() < fromInMillis + incrementInMillis).toList();
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



            Map<String, List<Transaction>> transactionsById = transactions.stream().collect(groupingBy(Transaction::getId));
            Map<String, List<Transaction>> transactionsByStatus = transactions.stream().collect(groupingBy(Transaction::getStatus));


            if (request.getMetrics() == null || request.getMetrics().isEmpty()) {


                metrics.addValue("transactionCount", BigDecimal.valueOf(transactionsById.size()));
                metrics.addValue("totalRequestsCount", BigDecimal.valueOf(requests.size()));
                metrics.addValue("totalAmountSpent", BigDecimal.valueOf(transactionsById.values().stream().filter(transactionList -> transactionList.size() > 1).map(transactionList2 -> transactionList2.get(0)).map(Transaction::getAmount).mapToDouble(Double::parseDouble).sum()));
                metrics.addValue("averageAmountSpent", BigDecimal.valueOf(transactionsById.values().stream().filter(transactionList -> transactionList.size() > 1).map(transactionList2 -> transactionList2.get(0)).map(Transaction::getAmount).mapToDouble(Double::parseDouble).average().orElse(0)));
                if (transactions.size() > 0) {

                    double successRate = getTransactionsRate(transactionsByStatus, true);
                    double failureRate = getTransactionsRate(transactionsByStatus, false);

                    metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(successRate));
                    metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(failureRate));

                } else {
                    metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(0));
                    metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(0));

                }
                double totalFees = transactionsById.values().stream().map(transactionList -> {
                    Transaction transaction = transactionList.stream().filter(transaction1 -> transaction1.getStatus().equals(TransactionStatus.SETTLED.getValue())).findFirst().orElse(null);
                    return transaction != null ? Double.parseDouble(transaction.getFees()) : 0;
                }).mapToDouble(Double::doubleValue).sum();

                double averageFees = getAverageFees(transactionsById);

                metrics.addValue("totalFees", BigDecimal.valueOf(totalFees));
                metrics.addValue("averageFees", BigDecimal.valueOf(averageFees));
                metrics.addValue("successfulRequestsCount", BigDecimal.valueOf(requests.stream().filter(request1 -> request1.getStatus().equals("SUCCESS")).count()));
                metrics.addValue("failedRequestsCount", BigDecimal.valueOf(requests.stream().filter(request1 -> request1.getStatus().equals("FAILED")).count()));
                metrics.addValue("successfulRequestsRate", BigDecimal.valueOf(getRequestsRate(requests,true)));
                metrics.addValue("failedRequestsRate", BigDecimal.valueOf(getRequestsRate(requests,false)));
                metrics.addValue("averageRequestTime", BigDecimal.valueOf(requests.stream().map(Request::getTime).mapToDouble(BigDecimal::doubleValue).average().orElse(0)));


            } else {
                for (String metric : request.getMetrics()) {
                    switch (metric) {
                        case "transactionCount" -> metrics.addValue("transactionCount", BigDecimal.valueOf(transactionsById.size()));
                        case "totalAmountSpent" -> metrics.addValue("totalAmountSpent", BigDecimal.valueOf(transactionsById.values().stream().map(transactionList -> transactionList.get(0)).map(Transaction::getAmount).mapToDouble(Double::parseDouble).sum()));
                        case "averageAmountSpent" -> metrics.addValue("averageAmountSpent", BigDecimal.valueOf(transactionsById.values().stream().map(transactionList -> transactionList.get(0)).map(Transaction::getAmount).mapToDouble(Double::parseDouble).average().orElse(0)));
                        case "TransactionSuccessRate" -> {
                            if (transactions.size() > 0) {
                                double successRate = getTransactionsRate(transactionsByStatus, true);

                                metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(successRate));

                            } else {
                                metrics.addValue("TransactionSuccessRate", BigDecimal.valueOf(0));
                            }
                        }
                        case "TransactionFailureRate" -> {
                            if (transactions.size() > 0) {
                                double failureRate = getTransactionsRate(transactionsByStatus, false);

                                metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(failureRate));

                            } else {
                                metrics.addValue("TransactionFailureRate", BigDecimal.valueOf(0));
                            }
                        }

                        case "totalFees" -> metrics.addValue("totalFees", BigDecimal.valueOf(transactionsById.values().stream().map(transactionList -> {
                            Transaction transaction = transactionList.stream().filter(transaction1 -> transaction1.getStatus().equals(TransactionStatus.SETTLED.getValue())).findFirst().orElse(null);
                            return transaction != null ? Double.parseDouble(transaction.getFees()) : 0;
                        }).mapToDouble(Double::doubleValue).sum()));
                        case "averageFees" -> metrics.addValue("averageFees", BigDecimal.valueOf(getAverageFees(transactionsById)));
                        case "totalRequestsCount" -> metrics.addValue("totalRequestsCount", BigDecimal.valueOf(requests.size()));
                        case "successfulRequestsCount" -> metrics.addValue("successfulRequestsCount", BigDecimal.valueOf(requests.stream().filter(request1 -> request1.getStatus().equals("SUCCESS")).count()));
                        case "failedRequestsCount" -> metrics.addValue("failedRequestsCount", BigDecimal.valueOf(requests.stream().filter(request1 -> request1.getStatus().equals("FAILED")).count()));
                        case "successfulRequestsRate" -> metrics.addValue("successfulRequestsRate", BigDecimal.valueOf(getRequestsRate(requests,true)));
                        case "failedRequestsRate" -> metrics.addValue("failedRequestsRate", BigDecimal.valueOf(getRequestsRate(requests,false)));
                        case "averageRequestTime" -> metrics.addValue("averageRequestTime", BigDecimal.valueOf(requests.stream().map(Request::getTime).mapToDouble(BigDecimal::doubleValue).average().orElse(0)));
                        default -> {
                            throw new IllegalArgumentException("metric not supported");
                        }
                    }

                }
            }
            metricsList.add(metrics);
            current = current.plus(increment, chronoUnit);
        }
        return metricsList;
    }

    private double getAverageFees(Map<String, List<Transaction>> transactionsById) {
        return transactionsById.values().stream().map(transactionList -> {
            Transaction transaction = transactionList.stream().filter(transaction1 -> transaction1.getStatus().equals(TransactionStatus.SETTLED.getValue())).findFirst().orElse(null);
            return transaction != null ? Double.parseDouble(transaction.getFees()) : 0;
        }).mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double getTransactionsRate(Map<String, List<Transaction>> transactionsByStatus, boolean success) {
        int successCount = transactionsByStatus.get(TransactionStatus.SETTLED.getValue()) != null ? transactionsByStatus.get(TransactionStatus.SETTLED.getValue()).size() : 0;
        int failureCount = transactionsByStatus.get(TransactionStatus.FAILED.getValue()) != null ? transactionsByStatus.get(TransactionStatus.FAILED.getValue()).size() : 0;
        int total = successCount + failureCount;
         return success ?  total > 0 ? 100 * (double) successCount / total : 0 : total > 0 ? 100 * (double) failureCount / total : 0;

    }

    private double getRequestsRate(List<Request> requests, boolean success) {
        int successCount = requests.stream().filter(request1 -> request1.getStatus().equals("SUCCESS")).toList().size();
        int failureCount = requests.stream().filter(request1 -> request1.getStatus().equals("FAILED")).toList().size();
        int total = successCount + failureCount;
        return success ?  total > 0 ? 100 * (double) successCount / total : 0 : total > 0 ? 100 * (double) failureCount / total : 0;
    }


    private List<Transaction> filterTransactionsByCreditCardType(List<Transaction> transactions, List<String> value) {
        return transactions.stream().filter(transaction -> transaction.getCreditCardType().equalsIgnoreCase(value.get(0))).toList();
    }

    private List<Transaction> filterTransactionsByStatus(List<Transaction> transactions, List<String> value) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (String status: value) {
            filteredTransactions.addAll(transactions.stream().filter(transaction -> transaction.getStatus().equalsIgnoreCase(status)).toList());
        }
        return filteredTransactions;
    }

    private Optional<Transaction> redisTransactionToTransaction(String redisTransaction) {

        Pattern idPattern = Pattern.compile("id='(.*?)'");
        Pattern IBANPattern = Pattern.compile("IBAN='(.*?)'");
        Pattern BICPattern = Pattern.compile("BIC='(.*?)'");
        Pattern isExternalPattern = Pattern.compile("isExternal=(.*?),");
        Pattern amountPattern = Pattern.compile("amount=(.*?),");
        Pattern feesPattern = Pattern.compile("fees=(.*?),");
        Pattern statusPattern = Pattern.compile("status=(.*?),");
        Pattern applicationIdPattern = Pattern.compile("applicationId=(.*?),");
        Pattern CreditCardTypePattern = Pattern.compile("creditCardType=(.*?),");
        Pattern timePattern = Pattern.compile("time=(.*?)}");



        Matcher idMatcher = idPattern.matcher(redisTransaction);
        Matcher IBANMatcher = IBANPattern.matcher(redisTransaction);
        Matcher BICMatcher = BICPattern.matcher(redisTransaction);
        Matcher isExternalMatcher = isExternalPattern.matcher(redisTransaction);
        Matcher amountMatcher = amountPattern.matcher(redisTransaction);
        Matcher feesMatcher = feesPattern.matcher(redisTransaction);
        Matcher statusMatcher = statusPattern.matcher(redisTransaction);
        Matcher applicationIdMatcher = applicationIdPattern.matcher(redisTransaction);
        Matcher CreditCardTypeMatcher = CreditCardTypePattern.matcher(redisTransaction);
        Matcher timeMatcher = timePattern.matcher(redisTransaction);

        if (idMatcher.find() && IBANMatcher.find() && BICMatcher.find() && isExternalMatcher.find() && amountMatcher.find() && feesMatcher.find() && statusMatcher.find() && timeMatcher.find() && applicationIdMatcher.find() && CreditCardTypeMatcher.find()) {

            Transaction transaction = new Transaction();
            transaction.setId(idMatcher.group(1));
            transaction.setRecipientIban(IBANMatcher.group(1));
            transaction.setRecipientBic(BICMatcher.group(1));
            if (IBANMatcher.find() && BICMatcher.find()) {
                transaction.setSenderBic(BICMatcher.group(1));
                transaction.setSenderIban(IBANMatcher.group(1));
            }
            transaction.setIs_external(Boolean.parseBoolean(isExternalMatcher.group(1)));
            transaction.setAmount(amountMatcher.group(1));
            transaction.setFees(feesMatcher.group(1));
            transaction.setStatus(statusMatcher.group(1));
            transaction.setApplicationId(Long.parseLong(applicationIdMatcher.group(1)));
            transaction.setCreditCardType(CreditCardTypeMatcher.group(1));
            transaction.setTime(Long.parseLong(timeMatcher.group(1)));

            return Optional.of(transaction);
        }

        return Optional.empty();

    }



}