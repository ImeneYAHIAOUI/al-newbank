package groupB.newbankV5.paymentgateway.controllers;

import groupB.newbankV5.paymentgateway.controllers.dto.ApplicationIntegrationDto;
import groupB.newbankV5.paymentgateway.entities.Application;
import groupB.newbankV5.paymentgateway.entities.Merchant;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationAlreadyExists;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.MerchantAlreadyExistsException;
import groupB.newbankV5.paymentgateway.exceptions.MerchantNotFoundException;
import groupB.newbankV5.paymentgateway.interfaces.IApplicationFinder;
import groupB.newbankV5.paymentgateway.interfaces.IApplicationIntegrator;
import groupB.newbankV5.paymentgateway.interfaces.IBusinessIntegrator;
import groupB.newbankV5.paymentgateway.interfaces.IRSA;
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
    public static final String BASE_URI = "/api/gateway/integration";
    private final IBusinessIntegrator businessIntegrator;
    private final IApplicationIntegrator applicationIntegrator;
    private final IApplicationFinder applicationFinder;
    private final IRSA crypto;

    @Autowired
    public IntegratorController(IBusinessIntegrator businessIntegrator, IApplicationIntegrator applicationIntegrator,
                                IApplicationFinder applicationFinder, IRSA crypto) {
        this.businessIntegrator = businessIntegrator;
        this.applicationIntegrator = applicationIntegrator;
        this.applicationFinder = applicationFinder;
        this.crypto = crypto;
    }

    @PostMapping("/merchants")
    public ResponseEntity<Merchant> integrateMerchant(@RequestBody Merchant merchant) throws MerchantAlreadyExistsException {
        log.info("Integrating merchant " + merchant.getName());
        return ResponseEntity.ok().body(businessIntegrator.integrateBusiness(merchant));
    }

    @PostMapping("/applications")
    public ResponseEntity<Application> integrateApplication(@RequestBody ApplicationIntegrationDto applicationIntegrationDto) throws ApplicationAlreadyExists,
            MerchantNotFoundException ,ApplicationNotFoundException{
        log.info("Integrating application " + applicationIntegrationDto.getName());
        Application application = new Application(applicationIntegrationDto.getName(),
                applicationIntegrationDto.getEmail(),
                applicationIntegrationDto.getUrl(),
                applicationIntegrationDto.getDescription());
        Merchant merchant = new Merchant();
        merchant.setId(applicationIntegrationDto.getMerchantId());
        return ResponseEntity.ok().body(applicationIntegrator.integrateApplication(application, merchant));
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

    @GetMapping("/applications/{id}/aeskey")
    public ResponseEntity<String> getAesKey(@PathVariable("id") Long id) throws ApplicationNotFoundException, NoSuchAlgorithmException {
        log.info("Getting AES key for application " + id);
        Application application = applicationFinder.findApplicationById(id);
        SecretKey aesKey = crypto.getOrGenerateAESKey(application);
        return ResponseEntity.ok().body(Base64.getEncoder().encodeToString(aesKey.getEncoded()));
    }


}
