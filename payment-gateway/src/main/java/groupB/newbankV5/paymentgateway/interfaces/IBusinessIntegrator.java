package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.entities.Merchant;
import groupB.newbankV5.paymentgateway.exceptions.MerchantAlreadyExistsException;

public interface IBusinessIntegrator {
    Merchant integrateBusiness(Merchant merchant) throws MerchantAlreadyExistsException;
}
