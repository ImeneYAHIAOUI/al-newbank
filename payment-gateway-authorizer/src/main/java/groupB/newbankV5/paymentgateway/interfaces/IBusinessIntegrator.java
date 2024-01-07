package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.connectors.dto.ApplicationDto;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentgateway.entities.Application;

public interface IBusinessIntegrator {

    ApplicationDto validateToken(String token) throws ApplicationNotFoundException, InvalidTokenException;
}
