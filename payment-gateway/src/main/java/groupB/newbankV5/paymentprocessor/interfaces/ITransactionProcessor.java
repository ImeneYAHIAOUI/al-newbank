package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.entities.Application;
import groupB.newbankV5.paymentprocessor.entities.CreditCard;
import groupB.newbankV5.paymentprocessor.entities.Transaction;
import groupB.newbankV5.paymentprocessor.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentprocessor.exceptions.InvalidTokenException;

import java.math.BigDecimal;

public interface ITransactionProcessor {
    Application validateToken(String token) throws InvalidTokenException, ApplicationNotFoundException;
    BigDecimal feesCalculator(Transaction transaction);
    void processPayment(String token, BigDecimal amount, CreditCard creditCard) throws InvalidTokenException, ApplicationNotFoundException;
}
