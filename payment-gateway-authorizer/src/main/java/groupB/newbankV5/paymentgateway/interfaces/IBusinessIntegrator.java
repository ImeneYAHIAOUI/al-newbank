package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.connectors.dto.AccountDto;
import groupB.newbankV5.paymentgateway.connectors.dto.ApplicationDto;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;

import java.math.BigDecimal;

public interface IBusinessIntegrator {

    ApplicationDto validateToken(String token) throws ApplicationNotFoundException, InvalidTokenException;
}
