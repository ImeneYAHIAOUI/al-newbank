package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.controllers.dto.*;
import groupB.newbankV5.paymentprocessor.entities.Transaction;


public interface ITransactionProcessor {
    PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails);
    TransferResponseDto authorizeTransfer(TransferDto transferDetails);

    CreditCardResponseDto validateCreditCard(CreditCardInformationDto paymentDetails);

    String reserveFunds(Transaction transaction);
}
