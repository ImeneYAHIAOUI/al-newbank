package groupB.newBankV5.statusreporter.connectors;


import groupB.newBankV5.statusreporter.connectors.dto.ApplicationDto;
import groupB.newBankV5.statusreporter.exceptions.ApplicationNotFoundException;
import groupB.newBankV5.statusreporter.exceptions.InvalidTokenException;
import groupB.newBankV5.statusreporter.interfaces.IBusinessIntegrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class BusinessIntegratorProxy implements IBusinessIntegrator {
    private static final Logger log = Logger.getLogger(BusinessIntegratorProxy.class.getName());
    @Value("${businessIntegrator.host.baseurl:}")
    private String integratorHostandPort;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ApplicationDto validateToken(String token) throws ApplicationNotFoundException,InvalidTokenException{
        ResponseEntity<ApplicationDto> responseEntity = restTemplate.getForEntity(integratorHostandPort + "/api/validation?token=" + token, ApplicationDto.class);
        ApplicationDto applicationDto = responseEntity.getBody();
        int statusCode = responseEntity.getStatusCodeValue();
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            throw new ApplicationNotFoundException("application not found");
        } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
            throw new InvalidTokenException("token invalid");
        }
        return applicationDto;
    }


}
