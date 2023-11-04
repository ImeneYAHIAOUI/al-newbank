package groupB.newbankV5.feescalculator.components;

import groupB.newbankV5.feescalculator.entities.CardType;
import groupB.newbankV5.feescalculator.entities.Transaction;
import groupB.newbankV5.feescalculator.entities.TransactionStatus;
import groupB.newbankV5.feescalculator.interfaces.IFeesCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class Calculator implements IFeesCalculator {
    private final Logger log = LoggerFactory.getLogger(Calculator.class);
    private static final double CREDIT_INTERCHANGE_FEE_RATE = 0.003;
    private static final double DEBIT_INTERCHANGE_FEE_RATE = 0.002;

    private static final double NETWORK_FEE_RATE = 0.000164;

    private static final double CROSS_BORDER_FEE_RATE = 0.0005;



    @Override
    public void applyFees(Transaction transaction) {
        log.info("Applying fees to transaction: " + transaction.getId());
        BigDecimal fees = BigDecimal.ZERO;
        if (transaction.getCreditCardType() == CardType.CREDIT) {
            fees = fees.add(transaction.getAmount().multiply(BigDecimal.valueOf(CREDIT_INTERCHANGE_FEE_RATE)));
        } else {
            fees = fees.add(transaction.getAmount().multiply(BigDecimal.valueOf(DEBIT_INTERCHANGE_FEE_RATE)));
        }

        if (!transaction.getSender().getBIC().startsWith("FR")) {
            fees = fees.add(transaction.getAmount().multiply(BigDecimal.valueOf(CROSS_BORDER_FEE_RATE)));
        }

        fees = fees.add(transaction.getAmount().multiply(BigDecimal.valueOf(NETWORK_FEE_RATE)));


        transaction.setFees(fees);
        transaction.setStatus(TransactionStatus.FEES_CALCULATED);
        log.info("Fees applied: " + transaction.getFees());
        log.info("Transaction status updated and sent to: " + transaction.toString());
    }

}
