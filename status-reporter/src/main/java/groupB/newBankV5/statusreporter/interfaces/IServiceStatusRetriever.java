package groupB.newBankV5.statusreporter.interfaces;

import groupB.newBankV5.statusreporter.entities.ServiceStatusWithMetrics;
import groupB.newBankV5.statusreporter.exceptions.ApplicationNotFoundException;
import groupB.newBankV5.statusreporter.exceptions.InvalidTokenException;

import java.util.List;

public interface IServiceStatusRetriever {
    List<ServiceStatusWithMetrics> retrieveServiceStatus(String tokien) throws InvalidTokenException, ApplicationNotFoundException;

    List<ServiceStatusWithMetrics> retrieveStatusFromPrometheus();

    ServiceStatusWithMetrics checkServiceAvailability(String serviceName);

    }
