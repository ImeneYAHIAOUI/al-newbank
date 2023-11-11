package groupB.newbankV5.businessIntegrator.interfaces;

import groupB.newbankV5.businessIntegrator.entities.Merchant;
import groupB.newbankV5.businessIntegrator.exceptions.MerchantAlreadyExistsException;

public interface IBusinessIntegrator {
    Merchant integrateBusiness(Merchant merchant) throws MerchantAlreadyExistsException;
    void deleteMerchants();
}
