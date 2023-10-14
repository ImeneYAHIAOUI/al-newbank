package groupB.newbankV5.paymentprocessor.controllers;

import groupB.newbankV5.paymentprocessor.controllers.dto.ApplicationIntegrationDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferResponseDto;
import groupB.newbankV5.paymentprocessor.entities.Application;
import groupB.newbankV5.paymentprocessor.entities.Merchant;
import groupB.newbankV5.paymentprocessor.exceptions.ApplicationAlreadyExists;
import groupB.newbankV5.paymentprocessor.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentprocessor.exceptions.MerchantAlreadyExistsException;
import groupB.newbankV5.paymentprocessor.exceptions.MerchantNotFoundException;
import groupB.newbankV5.paymentprocessor.interfaces.IApplicationIntegrator;
import groupB.newbankV5.paymentprocessor.interfaces.IBusinessIntegrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = IntegratorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class IntegratorController {
    private static final Logger log = Logger.getLogger(IntegratorController.class.getName());
    public static final String BASE_URI = "/api/gateway/integration";
    private final IBusinessIntegrator businessIntegrator;
    private final IApplicationIntegrator applicationIntegrator;

    @Autowired
    public IntegratorController(IBusinessIntegrator businessIntegrator, IApplicationIntegrator applicationIntegrator) {
        this.businessIntegrator = businessIntegrator;
        this.applicationIntegrator = applicationIntegrator;
    }

    @PostMapping("/merchants")
    public ResponseEntity<Merchant> integrateMerchant(@RequestBody Merchant merchant) throws MerchantAlreadyExistsException {
        log.info("Integrating merchant " + merchant.getName());
        return ResponseEntity.ok().body(businessIntegrator.integrateBusiness(merchant));
    }

    @PostMapping("/applications")
    public ResponseEntity<Application> integrateApplication(@RequestBody ApplicationIntegrationDto applicationIntegrationDto) throws ApplicationAlreadyExists, MerchantNotFoundException {
        log.info("Integrating application " + applicationIntegrationDto.getName());
        Application application = new Application(applicationIntegrationDto.getName(),
                applicationIntegrationDto.getEmail(),
                applicationIntegrationDto.getUrl(),
                applicationIntegrationDto.getDescription());
        Merchant merchant = new Merchant();
        merchant.setId(applicationIntegrationDto.getMerchantId());
        return ResponseEntity.ok().body(applicationIntegrator.integrateApplication(application, merchant));
    }

    @PostMapping("/applications/:id/token")
    public ResponseEntity<String> generateToken(@RequestParam("id") Long id) throws ApplicationNotFoundException {
        log.info("Generating token for application " + id);
        Application application = new Application();
        application.setId(id);
        return ResponseEntity.ok().body(applicationIntegrator.createOrRegenerateToken(application));
    }

    @GetMapping("/applications/:id/token")
    public ResponseEntity<String> getToken(@RequestParam("id") Long id) throws ApplicationNotFoundException {
        log.info("Getting token for application " + id);
        Application application = new Application();
        application.setId(id);
        return ResponseEntity.ok().body(applicationIntegrator.getToken(application));
    }

}
