package groupB.newBankV5.statusreporter.entities;

public class ServiceStatus {

    private String serviceName;
    private int serviceStatus;

    public ServiceStatus(String serviceName, int serviceStatus) {
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
