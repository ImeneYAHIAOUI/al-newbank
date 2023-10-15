package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.entities.Application;
import groupB.newbankV5.paymentgateway.entities.Merchant;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationAlreadyExists;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.MerchantNotFoundException;

public interface IApplicationIntegrator {
    Application integrateApplication(Application application, Merchant merchant) throws MerchantNotFoundException, ApplicationAlreadyExists;
    String createOrRegenerateToken(Application application) throws ApplicationNotFoundException;
    String getToken(Application application) throws ApplicationNotFoundException;
}
