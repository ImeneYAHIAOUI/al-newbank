package groupB.newbankV5.businessIntegrator.components;

import groupB.newbankV5.businessIntegrator.entities.Application;
import groupB.newbankV5.businessIntegrator.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.businessIntegrator.exceptions.InvalidTokenException;
import groupB.newbankV5.businessIntegrator.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.logging.Logger;
import groupB.newbankV5.businessIntegrator.repositories.ApplicationRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Service
public class Validator implements  IApplicationValidator{
    private static final Logger log = Logger.getLogger(Validator.class.getName());

    private ApplicationRepository applicationRepository;

    @Autowired
    public Validator( ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }
    @Override
    public Application validateToken(String token) throws InvalidTokenException, ApplicationNotFoundException {
        Jws<Claims> jws;
        try {
            jws = Jwts.parser().setSigningKey(Integrator.SECRET_KEY).parseClaimsJws(token);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token");
        }
        Claims bodyClaims = jws.getBody();
        Long applicationId = bodyClaims.get("id", Long.class);
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application with id " + applicationId + " not found"));

    }

}