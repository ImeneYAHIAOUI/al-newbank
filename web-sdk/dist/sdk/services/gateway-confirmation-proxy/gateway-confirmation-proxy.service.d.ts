import { RetrySettings } from "../Retry-settings";
import { StatusReporterProxyService } from "../status-reporter-proxy/status-reporter-proxy.service";
export declare class GatewayConfirmationProxyService {
    private readonly metricsProxy;
    private readonly _gatewayBaseUrl;
    private readonly retrySettings;
    private readonly config;
    private readonly statusReporterProxyService;
    private readonly _gatewayPath;
    constructor(load_balancer_host: string, retrySettings: RetrySettings, statusReporterProxyService: StatusReporterProxyService);
    confirmPayment(transactionId: string, token: string): Promise<String>;
}
