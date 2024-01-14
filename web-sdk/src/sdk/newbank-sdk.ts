import {PaymentService} from "./services/payment.service";
import {GetBackendStatus} from "./services/get-backend-status";
import {RetrySettings} from "./services/Retry-settings";
import {PaymentInfoDTO} from "./dto/payment-info.dto";
import {AuthorizeDto} from "./dto/authorise.dto";
import {MetricsServer} from "./services/Metrics-server";
import {BackendStatusDto} from "./dto/backend-status.dto";
import {MetricsDto} from "./dto/metrics.dto";
import {MetricRequestDto} from "./dto/metric-request.dto";

export class NewbankSdk {

    private readonly config = require('./services/config');


    

    private readonly _retrySettings: RetrySettings;
    private readonly _token: string;
    private readonly _paymentService: PaymentService;
    private readonly _getBackendStatus: GetBackendStatus;
    private readonly _metricsServer: MetricsServer;

    constructor(token: string,retrySettings ?: RetrySettings, responseTimeout ?: number) {
        this.config.maxTimeOut = responseTimeout || 4000;
        this._retrySettings = retrySettings || new RetrySettings();
        this._token = token;
        this._getBackendStatus = new GetBackendStatus(this._retrySettings);
        this._metricsServer = new MetricsServer(this._retrySettings);
        this._paymentService = new PaymentService(this._retrySettings);
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

    public async getMetrics(metricRequest: MetricRequestDto): Promise<MetricsDto[]> {
        return await this._metricsServer.getMetrics(metricRequest, this._token);
    }


}