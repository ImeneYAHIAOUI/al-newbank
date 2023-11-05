package groupB.newbankV5.businessIntegrator.interfaces;

import groupB.newbankV5.businessIntegrator.entities.Application;
import groupB.newbankV5.businessIntegrator.entities.Merchant;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationAlreadyExists;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.businessIntegrator.exceptions.MerchantNotFoundException;

public interface IApplicationIntegrator {
    Application integrateApplication(Application application, Merchant merchant) throws ApplicationNotFoundException, MerchantNotFoundException, ApplicationAlreadyExists;
    String createOrRegenerateToken(Application application) throws ApplicationNotFoundException;
    String getToken(Application application) throws ApplicationNotFoundException;
}
