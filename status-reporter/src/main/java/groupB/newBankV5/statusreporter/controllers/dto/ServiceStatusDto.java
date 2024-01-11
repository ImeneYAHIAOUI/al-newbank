package groupB.newBankV5.statusreporter.controllers.dto;

import groupB.newBankV5.statusreporter.entities.ServiceStatus;
import groupB.newBankV5.statusreporter.entities.ServiceStatusWithMetrics;

public class ServiceStatusDto {

    private int waitingTime;
    private String serviceName;
    private int serviceStatus;

    public ServiceStatusDto(String serviceName, int serviceStatus, int waitingTime) {
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
        this.waitingTime = waitingTime;
    }

    public ServiceStatusDto(String serviceName, int serviceStatus) {
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    static public ServiceStatusDto ServiceStatusDtoFactory(ServiceStatusWithMetrics serviceStatus) {
        return new ServiceStatusDto(serviceStatus.getServiceName(), serviceStatus.getServiceStatus(), serviceStatus.getWaitingTime());
    }
}
