package groupB.newBankV5.statusreporter.interfaces;

import groupB.newBankV5.statusreporter.entities.ServiceStatus;
import groupB.newBankV5.statusreporter.exceptions.ApplicationNotFoundException;
import groupB.newBankV5.statusreporter.exceptions.InvalidTokenException;

import java.util.List;

public interface IServiceStatusRetriever {
    List<ServiceStatus> retrieveServiceStatus(String tokien) throws InvalidTokenException, ApplicationNotFoundException;

    List<ServiceStatus> retrieveStatusFromPrometheus();

    boolean checkServiceAvailability(String serviceName);

    }
