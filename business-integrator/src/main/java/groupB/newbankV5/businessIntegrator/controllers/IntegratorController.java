package groupB.newbankV5.businessIntegrator.controllers;

import groupB.newbankV5.businessIntegrator.controllers.dto.ApplicationDto;
import groupB.newbankV5.businessIntegrator.controllers.dto.ApplicationIntegrationDto;
import groupB.newbankV5.businessIntegrator.controllers.dto.MerchantDto;
import groupB.newbankV5.businessIntegrator.entities.Application;
import groupB.newbankV5.businessIntegrator.entities.BankAccount;
import groupB.newbankV5.businessIntegrator.entities.Merchant;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationAlreadyExists;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.businessIntegrator.exceptions.MerchantAlreadyExistsException;
import groupB.newbankV5.businessIntegrator.exceptions.MerchantNotFoundException;
import groupB.newbankV5.businessIntegrator.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = IntegratorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class IntegratorController {
    private static final Logger log = Logger.getLogger(IntegratorController.class.getName());
    public static final String BASE_URI = "/api/integration";
    private final IBusinessIntegrator businessIntegrator;
    private final IBusinessFinder businessFinder;
    private final IApplicationIntegrator applicationIntegrator;
    private final IApplicationFinder applicationFinder;

    @Autowired
    public IntegratorController(IBusinessIntegrator businessIntegrator, IBusinessFinder businessFinder,
                                IApplicationIntegrator applicationIntegrator, IApplicationFinder applicationFinder) {
        this.businessIntegrator = businessIntegrator;
        this.businessFinder = businessFinder;
        this.applicationIntegrator = applicationIntegrator;
        this.applicationFinder = applicationFinder;
    }

    @GetMapping("/applications")
    public ResponseEntity<Application> getApplication(@RequestParam("name") String name) throws ApplicationNotFoundException {
        log.info("Getting application " + name);
        Application application = applicationFinder.findApplicationByName(name);
        return ResponseEntity.ok().body(application);
    }

    @GetMapping("/merchants/{id}")
    public ResponseEntity<Merchant> getMerchant(@PathVariable("id") Long id) throws MerchantNotFoundException {
        log.info("Getting merchant " + id);
        Merchant merchant = businessFinder.findMerchantById(id);
        return ResponseEntity.ok().body(merchant);
    }

    @GetMapping("/merchants")
    public ResponseEntity<BankAccount> getMerchantBankAccount(@RequestParam("name") String name) throws MerchantNotFoundException {
        log.info("Getting merchant " + name);
        return ResponseEntity.ok().body(businessFinder.findMerchantAccountByName(name));
    }
    @PostMapping("/merchants")
    public ResponseEntity<Merchant> integrateMerchant(@RequestBody MerchantDto merchantDto) throws MerchantAlreadyExistsException {
        log.info("Integrating merchant " + merchantDto.getName());
        Merchant merchant = new Merchant(merchantDto.getName(), merchantDto.getEmail(), merchantDto.getBankAccount());
        return ResponseEntity.ok().body(businessIntegrator.integrateBusiness(merchant));
    }

    @PostMapping("/applications")
    public ResponseEntity<ApplicationDto> integrateApplication(@RequestBody ApplicationIntegrationDto applicationIntegrationDto) throws ApplicationAlreadyExists,
            MerchantNotFoundException, ApplicationNotFoundException {
        log.info("Integrating application " + applicationIntegrationDto.getName());
        Application application = new Application(applicationIntegrationDto.getName(),
                applicationIntegrationDto.getEmail(),
                applicationIntegrationDto.getUrl(),
                applicationIntegrationDto.getDescription());
        Merchant merchant = new Merchant();
        merchant.setId(applicationIntegrationDto.getMerchantId());
        return ResponseEntity.ok().body(ApplicationDto.applicationDtoFactory(applicationIntegrator.integrateApplication(application, merchant)));
    }

    @PostMapping("/applications/{id}/token")
    public ResponseEntity<String> generateToken(@PathVariable("id") Long id) throws ApplicationNotFoundException {
        log.info("Generating token for application " + id);
        Application application = new Application();
        application.setId(id);
        return ResponseEntity.ok().body(applicationIntegrator.createOrRegenerateToken(application));
    }

    @GetMapping("/applications/{id}/token")
    public ResponseEntity<String> getToken(@PathVariable("id") Long id) throws ApplicationNotFoundException {
        log.info("Getting token for application " + id);
        Application application = new Application();
        application.setId(id);
        return ResponseEntity.ok().body(applicationIntegrator.getToken(application));
    }
}
