export class ServiceStatusWithMetricsDto {

    serviceName: string;
    serviceStatus: number;
    waitingTime: number;



    constructor(  serviceName: string, serviceStatus: number, waitingTime: number) {
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
        this.waitingTime = waitingTime;
    }



}