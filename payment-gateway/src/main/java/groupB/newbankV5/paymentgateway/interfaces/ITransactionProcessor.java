package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.entities.Application;
import groupB.newbankV5.paymentgateway.entities.CreditCard;
import groupB.newbankV5.paymentgateway.entities.Transaction;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.CCNException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;

import java.math.BigDecimal;

public interface ITransactionProcessor {
    Application validateToken(String token) throws InvalidTokenException, ApplicationNotFoundException;
    BigDecimal feesCalculator(Transaction transaction);
    void processPayment(String token, BigDecimal amount, CreditCard creditCard) throws InvalidTokenException, ApplicationNotFoundException, CCNException;
    void settlePayment(Transaction transaction) throws CCNException;
}
