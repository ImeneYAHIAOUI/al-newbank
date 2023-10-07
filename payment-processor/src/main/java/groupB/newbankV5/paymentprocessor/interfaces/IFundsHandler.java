package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferDto;

import java.math.BigDecimal;

public interface IFundsHandler {
    boolean hasSufficientFunds(String accountNumber, BigDecimal amount);

    boolean hasSufficientFunds(PaymentDetailsDTO paymentDetails);

    void deductFunds(double amount);

    boolean isFraudulent(TransferDto transaction);
}
