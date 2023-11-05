package groupB.newbankV5.anaytics.interfaces;

import groupB.newbankV5.anaytics.entities.Transaction;

public interface IPersister {
    void saveTransaction(Transaction transaction);
}
