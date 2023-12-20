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
        double fees = 0;
        if (transaction.getCreditCardType() == CardType.CREDIT) {
            fees += Double.parseDouble(transaction.getAmount()) * CREDIT_INTERCHANGE_FEE_RATE;
        } else {
            fees += Double.parseDouble(transaction.getAmount()) * DEBIT_INTERCHANGE_FEE_RATE;
        }

        if (!transaction.getSender().getBIC().startsWith("FR")) {
            fees += Double.parseDouble(transaction.getAmount()) * CROSS_BORDER_FEE_RATE;
        }

        fees += Double.parseDouble(transaction.getAmount()) * NETWORK_FEE_RATE;


        transaction.setFees(Double.toString(fees));
        transaction.setStatus(TransactionStatus.FEES_CALCULATED);

        log.info("Fees calculated: " + fees);
        log.info("Transaction " + transaction );
    }

}
