package groupB.newbankV5.mockcreditcardnetwork.controllers;

import groupB.newbankV5.mockcreditcardnetwork.components.CreditCardAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = SimulateTimeOutController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class SimulateTimeOutController {

    private static final Logger log = Logger.getLogger(SimulateTimeOutController.class.getName());
    public static final String BASE_URI = "/api/timeout";

    @Autowired
    private Environment environment;


    @PostMapping
    public void simulateTimeOut(){
        CreditCardAuthorizer.TIME_OUT_SIMULATION = true;
    }

}