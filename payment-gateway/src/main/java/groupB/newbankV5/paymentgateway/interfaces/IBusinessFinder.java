package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.entities.Merchant;
import groupB.newbankV5.paymentgateway.exceptions.MerchantNotFoundException;

public interface IBusinessFinder {
    Merchant findMerchantById(Long merchantId) throws MerchantNotFoundException;
}
