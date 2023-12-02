import {StatusReporterProxyService} from "./status-reporter-proxy/status-reporter-proxy.service";
import {RetrySettings} from "./Retry-settings";
import {BackendStatusDto} from "../dto/backend-status.dto";

export class GetBackendStatus{
    private readonly statusReporterProxy: StatusReporterProxyService;

    constructor(retrySettings: RetrySettings){
        this.statusReporterProxy = new StatusReporterProxyService(retrySettings);
    }

    async getBackendStatus(token: string): Promise<BackendStatusDto>{
        return await this.statusReporterProxy.reportStatus(token);
    }
}