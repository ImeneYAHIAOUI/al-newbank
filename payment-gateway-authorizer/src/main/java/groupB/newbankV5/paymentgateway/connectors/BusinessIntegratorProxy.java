package groupB.newbankV5.paymentgateway.connectors;

import groupB.newbankV5.paymentgateway.connectors.dto.ApplicationDto;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentgateway.interfaces.IBusinessIntegrator;
import groupB.newbankV5.paymentgateway.repository.ApplicationRepository;
import groupB.newbankV5.paymentgateway.entities.Application;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class BusinessIntegratorProxy implements IBusinessIntegrator {
    private static final Logger log = Logger.getLogger(BusinessIntegratorProxy.class.getName());

    private final ApplicationRepository applicationRepository;

    @Autowired
    public BusinessIntegratorProxy(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public ApplicationDto validateToken(String token) throws ApplicationNotFoundException,InvalidTokenException{


        Jws<Claims> jws;
        try {
            jws = Jwts.parser().setSigningKey("4242XX424208").parseClaimsJws(token);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token");
        }
        Claims bodyClaims = jws.getBody();
        Long applicationId = bodyClaims.get("id", Long.class);
        Application application= applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application with id " + applicationId + " not found"));

        return ApplicationDto.applicationDtoFactory(application);
    }


}
