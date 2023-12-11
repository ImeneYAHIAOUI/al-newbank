import {PaymentService} from "./services/payment.service";
import {GetBackendStatus} from "./services/get-backend-status";
import {RetrySettings} from "./services/Retry-settings";
import {PaymentInfoDTO} from "./dto/payment-info.dto";
import {AuthorizeDto} from "./dto/authorise.dto";
import {MetricsServer} from "./services/Metrics-server";
import {BackendStatusDto} from "./dto/backend-status.dto";
import {MetricsDto} from "./dto/metrics.dto";

export class NewbankSdk {

    

    private readonly _retrySettings: RetrySettings;
    private readonly _token: string;
    private readonly _paymentService: PaymentService;
    private readonly _getBackendStatus: GetBackendStatus;
    private readonly _metricsServer: MetricsServer;

    constructor(token: string,retrySettings: RetrySettings) {
        this._retrySettings = retrySettings;
        this._token = token;
        this._getBackendStatus = new GetBackendStatus(retrySettings);
        this._metricsServer = new MetricsServer(retrySettings);
        this._paymentService = new PaymentService(retrySettings);
    }
    
    public async authorizePayment(paymentInfo: PaymentInfoDTO): Promise<AuthorizeDto> {
        return await this._paymentService.authorize(paymentInfo, this._token);
        
    }
    
    public async confirmPayment(transactionId: string): Promise<String> {
        return await this._paymentService.confirmPayment(transactionId, this._token);
    }

    public async pay(paymentInfo: PaymentInfoDTO): Promise<void> {
        await this._paymentService.pay(paymentInfo, this._token);
    }
    
    public async getBackendStatus(): Promise<BackendStatusDto> {
        return await this._getBackendStatus.getBackendStatus(this._token);
    }

    public async getMetrics(metricRequest: any): Promise<MetricsDto[]> {
        return await this._metricsServer.getMetrics(metricRequest, this._token);
    }


}