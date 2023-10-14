package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.entities.Merchant;
import groupB.newbankV5.paymentprocessor.exceptions.MerchantAlreadyExistsException;

public interface IBusinessIntegrator {
    Merchant integrateBusiness(Merchant merchant) throws MerchantAlreadyExistsException;
}
