package groupB.newbankV5.paymentsettlement.interfaces;

import groupB.newbankV5.paymentsettlement.entities.Transaction;

public interface IPaymentProcessor {
    void saveTransactions(Transaction[] transactions);
}
