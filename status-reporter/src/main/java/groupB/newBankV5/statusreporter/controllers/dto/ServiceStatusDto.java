package groupB.newBankV5.statusreporter.controllers.dto;

import groupB.newBankV5.statusreporter.entities.ServiceStatus;

public class ServiceStatusDto {

    private String serviceName;
    private int serviceStatus;

    public ServiceStatusDto(String serviceName, int serviceStatus) {
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    static public ServiceStatusDto ServiceStatusDtoFactory(ServiceStatus serviceStatus) {
        return new ServiceStatusDto(serviceStatus.getServiceName(), serviceStatus.getServiceStatus());
    }
}
