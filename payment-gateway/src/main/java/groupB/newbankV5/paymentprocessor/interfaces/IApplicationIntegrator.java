package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.entities.Application;
import groupB.newbankV5.paymentprocessor.entities.Merchant;
import groupB.newbankV5.paymentprocessor.exceptions.ApplicationAlreadyExists;
import groupB.newbankV5.paymentprocessor.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentprocessor.exceptions.MerchantNotFoundException;

public interface IApplicationIntegrator {
    Application integrateApplication(Application application, Merchant merchant) throws MerchantNotFoundException, ApplicationAlreadyExists;
    String createOrRegenerateToken(Application application) throws ApplicationNotFoundException;
    String getToken(Application application) throws ApplicationNotFoundException;
}
