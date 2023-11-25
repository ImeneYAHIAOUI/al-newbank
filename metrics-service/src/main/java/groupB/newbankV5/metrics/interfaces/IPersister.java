package groupB.newbankV5.metrics.interfaces;

import groupB.newbankV5.metrics.entities.Transaction;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;

public interface IPersister {
    void saveTransaction(Transaction transaction);
    long getAuthorizedTransaction(String token) throws InvalidTokenException, ApplicationNotFoundException;
}
