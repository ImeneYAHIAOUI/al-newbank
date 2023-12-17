package groupB.newbankV5.paymentgateway.controllers;

import groupB.newbankV5.paymentgateway.entities.Transaction;
import groupB.newbankV5.paymentgateway.entities.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = SimulateTimeOutController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class SimulateTimeOutController {

    private static final Logger log = Logger.getLogger(TransactionerController.class.getName());
    public static final String BASE_URI = "/api/timeout";

    @Autowired
    private Environment environment;


    @GetMapping
    public ResponseEntity<String> find(){
        String variableValue = environment.getProperty("CCN_WITH_PORT");
        System.out.println(variableValue);

        String variableValue2 = environment.getProperty("$CCN_WITH_PORT");
        System.out.println(variableValue2);

        String variableValue3 = environment.getProperty("BUSINESSINTEGRATOR_SERVICE");
        System.out.println(variableValue3);
        return ResponseEntity.ok().body(variableValue3);
    }




}
