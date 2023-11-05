package groupB.newbankV5.businessIntegrator.interfaces;

import groupB.newbankV5.businessIntegrator.entities.Application;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationNotFoundException;

public interface IApplicationFinder {
    Application findApplicationById(Long id) throws ApplicationNotFoundException;

    Application findApplicationByName(String name) throws ApplicationNotFoundException;
}
