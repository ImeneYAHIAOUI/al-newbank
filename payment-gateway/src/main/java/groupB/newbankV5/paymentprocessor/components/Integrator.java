package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.entities.Application;
import groupB.newbankV5.paymentprocessor.entities.Merchant;
import groupB.newbankV5.paymentprocessor.exceptions.ApplicationAlreadyExists;
import groupB.newbankV5.paymentprocessor.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentprocessor.exceptions.MerchantAlreadyExistsException;
import groupB.newbankV5.paymentprocessor.exceptions.MerchantNotFoundException;
import groupB.newbankV5.paymentprocessor.interfaces.IApplicationIntegrator;
import groupB.newbankV5.paymentprocessor.interfaces.IBusinessIntegrator;
import groupB.newbankV5.paymentprocessor.repositories.ApplicationRepository;
import groupB.newbankV5.paymentprocessor.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class Integrator implements IBusinessIntegrator, IApplicationIntegrator {

    private static final Logger log = Logger.getLogger(Integrator.class.getName());
    public static String SECRET_KEY = "4242XX424208";
    private MerchantRepository merchantRepository;
    private ApplicationRepository applicationRepository;

    @Autowired
    public Integrator(MerchantRepository merchantRepository, ApplicationRepository applicationRepository) {
        this.merchantRepository = merchantRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application integrateApplication(Application application, Merchant merchant) throws MerchantNotFoundException, ApplicationAlreadyExists {
        Optional<Merchant> optMerchant = merchantRepository.findById(merchant.getId());
        if(optMerchant.isEmpty())
            throw new MerchantNotFoundException("Merchant with Id" + merchant.getId() + "not found");
        Optional<Application> optApplication = applicationRepository.findByNameOrUrl(application.getName(), application.getUrl());
        if(optApplication.isPresent())
            throw new ApplicationAlreadyExists("Application with name " + application.getName() + " or url " + application.getUrl() + " already exists");

        Merchant merchantFound = optMerchant.get();
        application.setMerchant(merchantFound);
        merchantFound.setApplication(application);

        // Cascading the save in merchant to application
        merchantRepository.saveAndFlush(merchant);
        return application;
    }

    @Override
    public String createOrRegenerateToken(Application application) throws ApplicationNotFoundException {
        Optional<Application> optApplication = applicationRepository.findById(application.getId());
        if(optApplication.isEmpty())
            throw new ApplicationNotFoundException("Application with Id " + application.getId() + " not found");
        Application applicationFound = optApplication.get();
        String token = applicationFound.generateToken();
        applicationRepository.saveAndFlush(applicationFound);
        return token;
    }

    @Override
    public String getToken(Application application) throws ApplicationNotFoundException {
        Optional<Application> optApplication = applicationRepository.findById(application.getId());
        if(optApplication.isEmpty())
            throw new ApplicationNotFoundException("Application with Id " + application.getId() + " not found");
        Application applicationFound = optApplication.get();
        return applicationFound.getApiKey();
    }

    @Override
    public Merchant integrateBusiness(Merchant merchant) throws MerchantAlreadyExistsException {
        Optional<Merchant> optMerchant = merchantRepository.findByNameOrEmail(merchant.getName(), merchant.getEmail());
        if(optMerchant.isPresent())
            throw new MerchantAlreadyExistsException("Merchant with name " + merchant.getName() +
                    " or email " + merchant.getEmail() + " already exists");
        return merchantRepository.saveAndFlush(merchant);
    }
}
