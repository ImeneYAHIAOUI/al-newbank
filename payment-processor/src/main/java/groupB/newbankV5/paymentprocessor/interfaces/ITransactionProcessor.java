package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentResponseDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferResponseDto;

public interface ITransactionProcessor {
    PaymentResponseDto authorizePayment(PaymentDetailsDTO paymentDetails);
    TransferResponseDto authorizeTransfer(TransferDto transferDetails);
}
