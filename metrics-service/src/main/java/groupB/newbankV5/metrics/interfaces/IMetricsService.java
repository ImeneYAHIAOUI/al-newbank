package groupB.newbankV5.metrics.interfaces;

import groupB.newbankV5.metrics.controllers.dto.MetricRequest;
import groupB.newbankV5.metrics.entities.Metrics;
import groupB.newbankV5.metrics.entities.Request;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;

import java.util.List;

public interface IMetricsService {


    List<Metrics> sendMetrics(String token, MetricRequest metrics) throws InvalidTokenException, ApplicationNotFoundException;


    Request createRequest(Request request, String token) throws InvalidTokenException, ApplicationNotFoundException;
}
