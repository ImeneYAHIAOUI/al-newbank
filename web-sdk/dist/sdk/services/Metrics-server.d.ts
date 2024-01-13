import { RetrySettings } from "./Retry-settings";
import { MetricsDto } from "../dto/metrics.dto";
import { MetricRequestDto } from "../dto/metric-request.dto";
export declare class MetricsServer {
    private readonly metricsProxy;
    constructor(retrySettings: RetrySettings);
    getMetrics(metricRequest: MetricRequestDto, token: string): Promise<MetricsDto[]>;
}
