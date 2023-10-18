package groupB.newbankV5.feescalculator.components;

import groupB.newbankV5.feescalculator.config.KafkaProducerService;
import groupB.newbankV5.feescalculator.entities.Transaction;
import groupB.newbankV5.feescalculator.entities.TransactionStatus;
import groupB.newbankV5.feescalculator.interfaces.IFeesCalculator;
import groupB.newbankV5.feescalculator.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class Calculator implements IFeesCalculator {
    private final Logger log = LoggerFactory.getLogger(Calculator.class);
    private final Integer FLAT_FEE = 10;
    private final double FEE_RATE = 0.03;
    private TransactionRepository transactionRepository;
    private KafkaProducerService kafkaProducerService;

    @Autowired
    public Calculator(TransactionRepository transactionRepository, KafkaProducerService kafkaProducerService) {
        this.transactionRepository = transactionRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void applyFees(Transaction transaction) {
        log.info("Applying fees to transaction: " + transaction.getId());
        BigDecimal fees = transaction.getAmount().compareTo(BigDecimal.valueOf(100)) > 0 ?
                transaction.getAmount().multiply(BigDecimal.valueOf(FEE_RATE)).add(BigDecimal.valueOf(FLAT_FEE)) :
                transaction.getAmount().multiply(BigDecimal.valueOf(FEE_RATE));
        transaction.setFees(fees);
        transaction.setStatus(TransactionStatus.FEES_CALCULATED);
        log.info("Fees applied: " + transaction.getFees());
        kafkaProducerService.sendMessage(transaction);
        log.info("Transaction status updated and sent to Kafka: " + transaction.toString());
    }

}
