package groupB.newBankV5.statusreporter.components;

import groupB.newBankV5.statusreporter.entities.ServiceStatus;

import java.util.List;

public interface IServiceStatusRetriever {
    List<ServiceStatus> retrieveServiceStatus();
}
