package groupB.newbankV5.metrics.controllers;

import groupB.newbankV5.metrics.components.MetricsService;
import groupB.newbankV5.metrics.controllers.dto.MetricRequest;

import groupB.newbankV5.metrics.entities.Metrics;
import groupB.newbankV5.metrics.entities.Request;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final Logger log = LoggerFactory.getLogger(MetricsController.class);

    private final MetricsService metricsService;

    @Autowired
    public MetricsController(MetricsService metrics) {
        this.metricsService = metrics;
    }



    @GetMapping("/health")
    public String health() {
        return "OK";
    }
    @PostMapping("")
    public ResponseEntity<List<Metrics>> getTransactions(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MetricRequest metricRequest) throws InvalidTokenException,
            ApplicationNotFoundException {
        String token = authorizationHeader.substring(7);

        return ResponseEntity.status(200).body(metricsService.sendMetrics(token, metricRequest));
    }

    @PostMapping("/request")
    public ResponseEntity<Request> createRequest(@RequestHeader("Authorization") String authorizationHeader,@RequestBody Request request) throws InvalidTokenException, ApplicationNotFoundException {
        String token = authorizationHeader.substring(7);
        return ResponseEntity.status(200).body(metricsService.createRequest(request, token));
    }






}
