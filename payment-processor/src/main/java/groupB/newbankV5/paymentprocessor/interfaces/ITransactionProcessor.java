package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.controllers.dto.*;

public interface ITransactionProcessor {
    PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails);
    TransferResponseDto authorizeTransfer(TransferDto transferDetails);

    CreditCardResponseDto validateCreditCard(CreditCardInformationDto paymentDetails);
}
