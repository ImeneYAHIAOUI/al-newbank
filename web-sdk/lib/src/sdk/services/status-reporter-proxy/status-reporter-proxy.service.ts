import {AuthorizeDto} from "../../dto/authorise.dto";
import {RetrySettings} from "../Retry-settings";
import * as retry from 'retry';
import axios, {AxiosRequestConfig, isAxiosError} from "axios";
import {HttpStatus} from "@nestjs/common";
import {UnauthorizedError} from "../../exceptions/unauthorized.exception";
import {ApplicationNotFound} from "../../exceptions/application-not-found.exception";
import {InternalServerError} from "../../exceptions/internal-server.exception";
import {BackendStatusDto} from "../../dto/backend-status.dto";



export class StatusReporterProxyService {


    private readonly statusReporteUrl: string;
    private readonly retrySettings: RetrySettings

    private readonly _statusReportePath = '/api/status/healthcheck';
    private readonly config;
    constructor( retrySettings: RetrySettings) {
        this.config = require('../config');
        this.statusReporteUrl = this.config.status_reporter_host;
        this.retrySettings = retrySettings;
    }

    async reportStatus( token: string): Promise<BackendStatusDto> {
        const operation = retry.operation({
            retries: this.retrySettings.retries,
            factor: this.retrySettings.factor,
            minTimeout: this.retrySettings.minTimeout,
            maxTimeout: this.retrySettings.maxTimeout,
            randomize: this.retrySettings.randomize,
        });
        let lastError: Error | undefined;
        return new Promise<BackendStatusDto>((resolve, reject) => {
            operation.attempt(async (currentAttempt) => {
                try {
                    const httpOptions: AxiosRequestConfig = {
                        headers: {
                            'Content-Type': 'application/json',
                            Authorization: `Bearer ${token}`,
                        },
                    };

                    const response = await axios.get(`${this.statusReporteUrl}${this._statusReportePath}`, httpOptions,);
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
                        console.error(lastError);
                        reject(lastError);
                    }
                }
            });
        });
    }

}