package groupB.newbankV5.businessIntegrator.interfaces;

import groupB.newbankV5.businessIntegrator.entities.BankAccount;
import groupB.newbankV5.businessIntegrator.entities.Merchant;
import groupB.newbankV5.businessIntegrator.exceptions.MerchantNotFoundException;

public interface IBusinessFinder {
    Merchant findMerchantById(Long merchantId) throws MerchantNotFoundException;
    BankAccount findMerchantAccountByName(String name) throws MerchantNotFoundException;

}
