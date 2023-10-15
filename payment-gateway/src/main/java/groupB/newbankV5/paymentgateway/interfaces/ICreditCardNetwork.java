package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.connectors.dto.CcnResponseDto;
import groupB.newbankV5.paymentgateway.connectors.dto.PaymentDetailsDTO;

public interface ICreditCardNetwork {
    CcnResponseDto authorizePayment(PaymentDetailsDTO paymentDetailsDTO);
}
