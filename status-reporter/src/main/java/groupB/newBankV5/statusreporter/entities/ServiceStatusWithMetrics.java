package groupB.newBankV5.statusreporter.entities;

public class ServiceStatusWithMetrics {

    private String serviceName;
    private int serviceStatus;
    private int waitingTime;

    public ServiceStatusWithMetrics(String serviceName, int serviceStatus, int waitingTime) {
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
        this.waitingTime = waitingTime;
    }

    public ServiceStatusWithMetrics() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceStatusWithMetrics)) return false;

        ServiceStatusWithMetrics that = (ServiceStatusWithMetrics) o;

        if (getServiceStatus() != that.getServiceStatus()) return false;
        if (getWaitingTime() != that.getWaitingTime()) return false;
        return getServiceName() != null ? getServiceName().equals(that.getServiceName()) : that.getServiceName() == null;
    }

    @Override
    public int hashCode() {
        int result = getServiceName() != null ? getServiceName().hashCode() : 0;
        result = 31 * result + getServiceStatus();
        result = 31 * result + getWaitingTime();
        return result;
    }

    @Override
    public String toString() {
        return "ServiceStatusWithMetrics{" +
                "serviceName='" + serviceName + '\'' +
                ", serviceStatus=" + serviceStatus +
                ", waitingTime=" + waitingTime +
                '}';
    }
}

