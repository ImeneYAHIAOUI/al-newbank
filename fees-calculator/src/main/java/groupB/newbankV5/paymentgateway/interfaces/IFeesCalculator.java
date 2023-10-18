package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.entities.Transaction;

import java.math.BigDecimal;

public interface IFeesCalculator {
    BigDecimal feesCalculator(Transaction transaction);

}
