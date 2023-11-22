package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.entities.Transaction;

import java.math.BigDecimal;

public interface IPaymentProcessor {
    String reserveFunds(Transaction transaction);
}
