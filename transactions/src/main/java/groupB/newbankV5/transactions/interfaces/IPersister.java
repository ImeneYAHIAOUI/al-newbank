package groupB.newbankV5.transactions.interfaces;

import groupB.newbankV5.transactions.entities.Transaction;

public interface IPersister {
    void saveTransaction(Transaction transaction);
}
