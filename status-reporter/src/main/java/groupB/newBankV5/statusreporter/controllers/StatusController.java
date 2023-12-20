package groupB.newBankV5.statusreporter.controllers;

import groupB.newBankV5.statusreporter.exceptions.ApplicationNotFoundException;
import groupB.newBankV5.statusreporter.exceptions.InvalidTokenException;
import groupB.newBankV5.statusreporter.interfaces.IServiceStatusRetriever;
import groupB.newBankV5.statusreporter.controllers.dto.ServiceStatusDto;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = StatusController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class StatusController {

    private static final Logger log = LoggerFactory.getLogger(StatusController.class.getName());
    public static final String BASE_URI = "/api/status";

    private final MeterRegistry meterRegistry;

    private final IServiceStatusRetriever serviceStatusRetriever;

    @Autowired
    public StatusController(MeterRegistry meterRegistry, IServiceStatusRetriever serviceStatusRetriever) {
        this.meterRegistry = meterRegistry;
        this.serviceStatusRetriever = serviceStatusRetriever;
    }

    @Timed(value = "up_check", description = "Up Check Indicator")
    @GetMapping("/healthcheck")
    public ResponseEntity<List<ServiceStatusDto>> healthcheck(@RequestHeader("Authorization") String authorizationHeader ) throws InvalidTokenException, ApplicationNotFoundException {
        // Remove the "Bearer " prefix
        String token = authorizationHeader.substring(7);
        List<ServiceStatusDto> serviceStatus = serviceStatusRetriever.retrieveServiceStatus(token).stream()
                .map(ServiceStatusDto::ServiceStatusDtoFactory)
                .toList();
        return ResponseEntity.ok(serviceStatus);
    }

    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkServiceAvailability(@RequestParam String serviceName) {
        return ResponseEntity.ok(serviceStatusRetriever.checkServiceAvailability(serviceName));
    }
}
