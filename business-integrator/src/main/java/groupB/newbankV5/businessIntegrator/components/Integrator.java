package groupB.newbankV5.businessIntegrator.components;

import groupB.newbankV5.businessIntegrator.entities.Application;
import groupB.newbankV5.businessIntegrator.entities.BankAccount;
import groupB.newbankV5.businessIntegrator.entities.Merchant;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationAlreadyExists;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.businessIntegrator.exceptions.MerchantAlreadyExistsException;
import groupB.newbankV5.businessIntegrator.exceptions.MerchantNotFoundException;
import groupB.newbankV5.businessIntegrator.interfaces.IApplicationFinder;
import groupB.newbankV5.businessIntegrator.interfaces.IApplicationIntegrator;
import groupB.newbankV5.businessIntegrator.interfaces.IBusinessFinder;
import groupB.newbankV5.businessIntegrator.interfaces.IBusinessIntegrator;
import groupB.newbankV5.businessIntegrator.repositories.ApplicationRepository;
import groupB.newbankV5.businessIntegrator.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class Integrator implements IBusinessIntegrator, IApplicationIntegrator, IApplicationFinder, IBusinessFinder {

    private static final Logger log = Logger.getLogger(Integrator.class.getName());
    public static String SECRET_KEY = "4242XX424208";
    private MerchantRepository merchantRepository;
    private ApplicationRepository applicationRepository;

    @Autowired
    public Integrator(MerchantRepository merchantRepository, ApplicationRepository applicationRepository) {
        this.merchantRepository = merchantRepository;
        this.applicationRepository = applicationRepository;
    }

    public BankAccount findMerchantAccountByName(String name) throws MerchantNotFoundException{
        Optional<Merchant> optMerchant = merchantRepository.findByName(name);
        if(optMerchant.isEmpty()){
            throw new MerchantNotFoundException("Merchant with name " + name + " not found");
        }

        return optMerchant.get().getBankAccount();

    }
  
    @Override
    public Application findApplicationById(Long id) throws ApplicationNotFoundException {
        Optional<Application> optApplication = applicationRepository.findById(id);
        if(optApplication.isEmpty())
            throw new ApplicationNotFoundException("Application with Id " + id + " not found");
        return optApplication.get();
    }

    @Override
    public Application findApplicationByName(String name) throws ApplicationNotFoundException {
        Optional<Application> optApplication = applicationRepository.findByName(name);
        if(optApplication.isEmpty())
            throw new ApplicationNotFoundException("Application with name " + name + " not found");
        return optApplication.get();
    }

    @Override
    public Merchant findMerchantById(Long id) throws MerchantNotFoundException {
        Optional<Merchant> optMerchant = merchantRepository.findById(id);
        if(optMerchant.isEmpty())
            throw new MerchantNotFoundException("Merchant with Id " + id + " not found");
        return optMerchant.get();
    }
    @Override
    public void deleteMerchants(){
        this.merchantRepository.deleteAll();
    }

    @Override
    public Application integrateApplication(Application application, Merchant merchant) throws MerchantNotFoundException, ApplicationAlreadyExists,ApplicationNotFoundException {
        Optional<Merchant> optMerchant = merchantRepository.findById(merchant.getId());
        if(optMerchant.isEmpty())
            throw new MerchantNotFoundException("Merchant with Id" + merchant.getId() + "not found");
        Optional<Application> optApplication = applicationRepository.findByNameOrUrl(application.getName(), application.getUrl());
        if(optApplication.isPresent())
            throw new ApplicationAlreadyExists("Application with name " + application.getName() + " or url " + application.getUrl() + " already exists");
        Merchant merchantFound = optMerchant.get();
        application.setMerchant(merchantFound);
        merchantFound.setApplication(application);

        application = applicationRepository.saveAndFlush(application);
        merchantRepository.saveAndFlush(merchantFound);

        application.generateToken();
        Application applicationFound = applicationRepository.saveAndFlush(application);
        log.info("\u001B[32mApplication " + application.getName() + " integrated\u001B[0m");
        return applicationFound;
    }

    @Override
    public String createOrRegenerateToken(Application application) throws ApplicationNotFoundException {
        Optional<Application> optApplication = applicationRepository.findByName(application.getName());
        if(optApplication.isEmpty())
            throw new ApplicationNotFoundException("Application with name " + application.getName() + " not found");
        Application applicationFound = optApplication.get();
        String token = applicationFound.generateToken();
        applicationRepository.saveAndFlush(applicationFound);
        return token;
    }
    @Override
    public String getToken(Application application) throws ApplicationNotFoundException {
        Optional<Application> optApplication = applicationRepository.findByName(application.getName());
        if(optApplication.isEmpty())
            throw new ApplicationNotFoundException("Application with name " + application.getName() + " not found");
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
