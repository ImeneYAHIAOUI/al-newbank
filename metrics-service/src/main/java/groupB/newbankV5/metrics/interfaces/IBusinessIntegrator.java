package groupB.newbankV5.metrics.interfaces;

import groupB.newbankV5.metrics.controllers.dto.ApplicationDto;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;

public interface IBusinessIntegrator {

    ApplicationDto validateToken(String token) throws ApplicationNotFoundException, InvalidTokenException;
}
