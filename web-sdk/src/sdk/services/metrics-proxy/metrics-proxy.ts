import {RetrySettings} from "../Retry-settings";
import * as retry from "retry";
import axios, {AxiosRequestConfig} from "axios";
import {HttpStatus} from "@nestjs/common";
import {UnauthorizedError} from "../../exceptions/unauthorized.exception";
import {ApplicationNotFound} from "../../exceptions/application-not-found.exception";
import {InternalServerError} from "../../exceptions/internal-server.exception";
import {MetricRequestDto} from "../../dto/metric-request.dto";
import {MetricsDto} from "../../dto/metrics.dto";
import {RequestDto} from "../../dto/request.dto";

export class MetricsProxy {
    private readonly metricsUrl: string;
    private readonly retrySettings: RetrySettings

    private readonly _metricsPath = '/api/metrics';
    private readonly config;

    constructor(retrySettings: RetrySettings) {
        this.config = require('../config');
        this.metricsUrl = this.config.metrics_host;
        this.retrySettings = retrySettings;
    }

    async retrieveMetrics(metricRequest: MetricRequestDto, token: string): Promise<MetricsDto[]> {

        const operation = retry.operation({
            retries: this.retrySettings.retries,
            factor: this.retrySettings.factor,
            minTimeout: this.retrySettings.minTimeout,
            maxTimeout: this.retrySettings.maxTimeout,
            randomize: this.retrySettings.randomize,
        });

        let lastError: Error | undefined;

        return new Promise<MetricsDto[]>((resolve, reject) => {
            operation.attempt(async (currentAttempt) => {
                try {
                    const httpOptions: AxiosRequestConfig = {
                        headers: {
                            'Content-Type': 'application/json',
                            Authorization: `Bearer ${token}`,
                        },
                    };

                    const response = await axios.post(
                        `${this.metricsUrl}${this._metricsPath}`,
                        metricRequest,
                        httpOptions,
                    );
                    resolve(response.data);
                } catch (error: any) {
                    lastError = error;
                    if (operation.retry(lastError)) {
                        console.error(`Retry attempt ${currentAttempt} failed. Retrying...`);
                        return;
                    }
                    console.error(`All retry attempts failed. Last error: ${lastError?.message}`);
                    if (isAxiosError(lastError) && lastError.response) {

                        if (lastError.response.status === HttpStatus.UNAUTHORIZED) {
                            console.error(lastError.response);
                            console.error(`Unauthorized access`);
                            reject(new UnauthorizedError(lastError.message));
                        } else if (lastError.response.status === HttpStatus.NOT_FOUND) {
                            console.error(lastError.response);
                            console.error(`Application not found`);
                            reject(new ApplicationNotFound());
                        } else if (lastError.response.status === HttpStatus.INTERNAL_SERVER_ERROR) {
                            console.error(lastError.response);
                            reject(new InternalServerError(lastError.message));
                        }
                    } else {
                        const errorMessage = `Error while processing payment: ${lastError?.message}`;
                        console.error(errorMessage);
                        reject(new Error(errorMessage));
                    }
                }
            });
        });
    }
    async sendRequestResult(requestDto: RequestDto, token: string){
        try {
            const httpOptions: AxiosRequestConfig = {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
            };

            await axios.post(`${this.metricsUrl}${this._metricsPath}/request`, requestDto, httpOptions);
        } catch (error: any) {
            console.error(`Error while processing request: ${error.message}`);
        }
    }

}
function isAxiosError(error: any): error is { isAxiosError: boolean; response: any } {
    return error.isAxiosError === true && error.response !== undefined;
}