import axios, { AxiosError, AxiosRequestConfig } from 'axios';
import * as retry from 'retry';
import { HttpStatus } from '@nestjs/common';

import { ApplicationNotFound } from '../../exceptions/application-not-found.exception';
import { InternalServerError } from '../../exceptions/internal-server.exception';
import { UnauthorizedError } from '../../exceptions/unauthorized.exception';
import {RetrySettings} from "../Retry-settings";
import {MetricsProxy} from "../metrics-proxy/metrics-proxy";
import {RequestDto} from "../../dto/request.dto";
import {StatusReporterProxyService} from "../status-reporter-proxy/status-reporter-proxy.service";
import { ServiceUnavailableException } from '../../exceptions/service-unavailable-exception';


export class GatewayConfirmationProxyService {
  private readonly metricsProxy: MetricsProxy;
  private readonly _gatewayBaseUrl: string;
  private readonly retrySettings: RetrySettings
  private readonly config = require('./../config');
  private readonly statusReporterProxyService: StatusReporterProxyService;
  private readonly _gatewayPath = '/api/gateway-confirmation';
  constructor(load_balancer_host: string,retrySettings: RetrySettings, statusReporterProxyService: StatusReporterProxyService) {
    this._gatewayBaseUrl = `${load_balancer_host}`;
    this.retrySettings = retrySettings;
    this.metricsProxy = new MetricsProxy(retrySettings)
    this.statusReporterProxyService = statusReporterProxyService;
  }
 
   confirmPayment(transactionId: string, token: string): Promise<String> {
        const operation = retry.operation({
          retries: this.retrySettings.retries,
          factor: this.retrySettings.factor,
          minTimeout: this.retrySettings.minTimeout,
          maxTimeout: this.retrySettings.maxTimeout,
          randomize: this.retrySettings.randomize,
        });
        let lastError: Error | undefined;

       const start = new Date().getTime();

        return new Promise<String>((resolve, reject) => {
          operation.attempt(async (currentAttempt) => {
            try {
              const httpOptions: AxiosRequestConfig = {
                headers: {
                  'Content-Type': 'application/json',
                  Authorization: `Bearer ${token}`,
                },
              };

              const serviceMetrics = await this.statusReporterProxyService.checkAvailability(this.config.service_confirmation_name);
              if (serviceMetrics.waitingTime > 0) {
                reject(new ServiceUnavailableException('The server is currently under backpressure.', serviceMetrics.waitingTime));
                return;
              }
      
               const response = await axios.post(`${this._gatewayBaseUrl}${this._gatewayPath}/${transactionId}`,
               {
                ...httpOptions, timeout: this.config.maxTimeOut,}
              );
              resolve(response.data);
                const end = new Date().getTime();
                const time = end - start;
                const request : RequestDto = new RequestDto(new Date().toISOString(), time, 'SUCCESS', 'Payment confirmed');
                await this.metricsProxy.sendRequestResult(request, token);
            } catch (error: any) {
              lastError = error;
                const end = new Date().getTime()
                const time = end - start;

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
                            reject(new ApplicationNotFound(lastError.response));
                          } else if (lastError.response.status === HttpStatus.INTERNAL_SERVER_ERROR) {
                            console.error(lastError.response);
                            reject(new InternalServerError(lastError.message));
                          }
                        } else {
                          const errorMessage = `Error while processing payment: ${lastError?.message}`;
                          console.error(errorMessage);
                          reject(new Error(errorMessage));
                        }
                const request : RequestDto = new RequestDto(new Date().toISOString(), time, 'FAILED', 'Payment confirmation failed');
                await this.metricsProxy.sendRequestResult(request, token);


                      }
          });
        });

}
}
function isAxiosError(error: any): error is { isAxiosError: boolean; response: any } {
  return error.isAxiosError === true && error.response !== undefined;
}