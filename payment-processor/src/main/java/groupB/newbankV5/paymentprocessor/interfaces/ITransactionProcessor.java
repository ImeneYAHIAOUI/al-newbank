package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.controllers.dto.*;

import java.math.BigDecimal;

public interface ITransactionProcessor {
    PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails);
    TransferResponseDto authorizeTransfer(TransferDto transferDetails);

    CreditCardResponseDto validateCreditCard(CreditCardInformationDto paymentDetails);

    void reserveFunds(BigDecimal amount, String cardNumber, String expirationDate, String cvv);
}
