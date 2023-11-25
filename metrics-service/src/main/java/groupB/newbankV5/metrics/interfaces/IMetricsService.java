package groupB.newbankV5.metrics.interfaces;

import groupB.newbankV5.metrics.entities.Transaction;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;

public interface IMetricsService {
    long getAuthorizedTransaction(String token) throws InvalidTokenException, ApplicationNotFoundException;
    long getConfirmedTransaction(String token) throws InvalidTokenException, ApplicationNotFoundException;
}
