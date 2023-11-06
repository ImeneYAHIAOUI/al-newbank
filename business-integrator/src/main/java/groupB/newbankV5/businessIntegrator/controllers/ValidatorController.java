package groupB.newbankV5.businessIntegrator.controllers;

import groupB.newbankV5.businessIntegrator.controllers.dto.ApplicationDto;
import groupB.newbankV5.businessIntegrator.controllers.dto.ApplicationIntegrationDto;
import groupB.newbankV5.businessIntegrator.controllers.dto.MerchantDto;
import groupB.newbankV5.businessIntegrator.entities.Application;
import groupB.newbankV5.businessIntegrator.entities.Merchant;
import groupB.newbankV5.businessIntegrator.exceptions.InvalidTokenException;

import groupB.newbankV5.businessIntegrator.exceptions.ApplicationAlreadyExists;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.businessIntegrator.exceptions.MerchantAlreadyExistsException;
import groupB.newbankV5.businessIntegrator.exceptions.MerchantNotFoundException;
import groupB.newbankV5.businessIntegrator.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = ValidatorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class ValidatorController {
    private static final Logger log = Logger.getLogger(ValidatorController.class.getName());
    public static final String BASE_URI = "/api/validation";
    private final IApplicationValidator applicationValidator;
    private final IApplicationIntegrator applicationIntegrator;


    @Autowired
    public ValidatorController(IApplicationValidator applicationValidator,
                                IApplicationIntegrator applicationIntegrator
                              ) {
        this.applicationValidator = applicationValidator;
        this.applicationIntegrator = applicationIntegrator;
    }

    @GetMapping()
    public ResponseEntity<ApplicationDto> validateToken(@RequestParam("token") String token) throws InvalidTokenException, ApplicationNotFoundException {
        Application application = applicationValidator.validateToken(token);
        ApplicationDto applicationDto =  ApplicationDto.applicationDtoFactory(application);
        return ResponseEntity.ok().body(applicationDto);
    }





}
