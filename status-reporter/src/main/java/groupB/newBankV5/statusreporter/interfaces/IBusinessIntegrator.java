package groupB.newBankV5.statusreporter.interfaces;


import groupB.newBankV5.statusreporter.connectors.dto.ApplicationDto;
import groupB.newBankV5.statusreporter.exceptions.ApplicationNotFoundException;
import groupB.newBankV5.statusreporter.exceptions.InvalidTokenException;

public interface IBusinessIntegrator {

    ApplicationDto validateToken(String token) throws ApplicationNotFoundException, InvalidTokenException;
}
