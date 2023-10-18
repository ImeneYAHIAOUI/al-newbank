package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;

public interface IFraudDetector {
    boolean isFraudulent(PaymentDetailsDTO transaction);
}
