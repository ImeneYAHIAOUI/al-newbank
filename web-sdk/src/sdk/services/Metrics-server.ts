import {MetricsProxy} from "./metrics-proxy/metrics-proxy";
import {RetrySettings} from "./Retry-settings";
import {MetricsDto} from "../dto/metrics.dto";
import {MetricRequestDto} from "../dto/metric-request.dto";


export class MetricsServer {
  private readonly metricsProxy: MetricsProxy;

    constructor(retrySettings: RetrySettings) {
        this.metricsProxy = new MetricsProxy(retrySettings);
    }

    async getMetrics(metricRequest : MetricRequestDto, token: string): Promise<MetricsDto[]> {
        return await this.metricsProxy.retrieveMetrics(metricRequest,token);
    }




  
}



