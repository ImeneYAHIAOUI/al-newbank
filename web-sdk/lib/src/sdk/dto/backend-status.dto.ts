class BackendServiceStatus {
    serviceName: string;
    serviceStatus: string;
    constructor(serviceName: string, serviceStatus: string) {
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
    }
}

export class BackendStatusDto {
    backendServicesStatus: BackendServiceStatus[];

    constructor(backendServicesStatus: BackendServiceStatus[]) {
        this.backendServicesStatus = backendServicesStatus;
    }
}