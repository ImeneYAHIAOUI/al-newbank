package groupB.newbankV5.feescalculator.interfaces;

import groupB.newbankV5.feescalculator.entities.Transaction;

import java.math.BigDecimal;

public interface IFeesCalculator {
    BigDecimal feesCalculator(Transaction transaction);

}
