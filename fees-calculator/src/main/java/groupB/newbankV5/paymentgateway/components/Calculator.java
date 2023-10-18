package groupB.newbankV5.paymentgateway.components;

import groupB.newbankV5.paymentgateway.entities.Transaction;
import groupB.newbankV5.paymentgateway.interfaces.IFeesCalculator;
import groupB.newbankV5.paymentgateway.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class Calculator implements IFeesCalculator {
    private final Integer FEE_RATE = 10;
    private final double FLAT_FEE = 0.03;

    private TransactionRepository transactionRepository;

    @Autowired
    public Calculator(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public BigDecimal feesCalculator(Transaction transaction) {
        return transaction.getAmount().compareTo(BigDecimal.valueOf(100)) > 0 ?
                transaction.getAmount().multiply(BigDecimal.valueOf(FLAT_FEE)).add(BigDecimal.valueOf(FEE_RATE)) :
                transaction.getAmount().multiply(BigDecimal.valueOf(FLAT_FEE));
    }

}
