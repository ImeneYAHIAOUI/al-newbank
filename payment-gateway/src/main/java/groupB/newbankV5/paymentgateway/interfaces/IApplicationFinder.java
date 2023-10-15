package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.entities.Application;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;

public interface IApplicationFinder {
    Application findApplicationById(Long id) throws ApplicationNotFoundException;
}
