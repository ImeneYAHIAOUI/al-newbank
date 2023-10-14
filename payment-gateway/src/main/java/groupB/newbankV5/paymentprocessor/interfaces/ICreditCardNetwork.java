package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.connectors.dto.CcnResponseDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;

public interface ICreditCardNetwork {
    CcnResponseDto authorizePayment(PaymentDetailsDTO paymentDetailsDTO);
}
