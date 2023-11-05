package groupB.newbankV5.businessIntegrator.interfaces;

import groupB.newbankV5.businessIntegrator.entities.Application;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.businessIntegrator.exceptions.InvalidTokenException;
public interface IApplicationValidator {
    Application validateToken(String token) throws InvalidTokenException, ApplicationNotFoundException;
}
