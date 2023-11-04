package groupB.newbankV5.paymentsettlement.interfaces;

import groupB.newbankV5.paymentsettlement.entities.Transaction;

import java.util.List;

public interface FeesCalculator {
    Transaction[] calculateFees(Transaction[] transactions);
}
