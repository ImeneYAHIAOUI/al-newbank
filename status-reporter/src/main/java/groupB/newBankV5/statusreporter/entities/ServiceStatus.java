package groupB.newBankV5.statusreporter.entities;

public class ServiceStatus {

    private String serviceName;
    private String serviceStatus;

    public ServiceStatus(String serviceName, String serviceStatus) {
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }
}
