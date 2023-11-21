package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.connectors.dto.CcnResponseDto;
import groupB.newbankV5.paymentgateway.connectors.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;

public interface ITransactionFinder {
    long getConfirmedTransaction(String token) throws InvalidTokenException, ApplicationNotFoundException;
    long getAuthorizedTransaction(String token) throws InvalidTokenException, ApplicationNotFoundException;

}
