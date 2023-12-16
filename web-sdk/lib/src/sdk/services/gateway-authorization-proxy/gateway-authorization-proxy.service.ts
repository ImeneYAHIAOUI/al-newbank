import axios, { AxiosError, AxiosRequestConfig } from 'axios';
import * as retry from 'retry';
import { HttpStatus } from '@nestjs/common';
import { MerchantDTO } from '../../dto/merchant.dto';
import { ApplicationDto } from '../../dto/application.dto';
import { MerchantAlreadyExists } from '../../exceptions/merchant-already-exists.exception';
import { ApplicationNotFound } from '../../exceptions/application-not-found.exception';
import { InternalServerError } from '../../exceptions/internal-server.exception';
import { UnauthorizedError } from '../../exceptions/unauthorized.exception';
import {AuthorizeDto} from "../../dto/authorise.dto";
import {RetrySettings} from "../Retry-settings";
import { StatusReporterProxyService } from '../status-reporter-proxy/status-reporter-proxy.service';

import {MetricsProxy} from "../metrics-proxy/metrics-proxy";
import {RequestDto} from "../../dto/request.dto";
export class GatewayAuthorizationProxyService {
  private readonly _gatewayBaseUrl: string;
  private readonly _gatewayPath = '/api/gateway_authorization/';
  private readonly retrySettings: RetrySettings;
  private readonly metricsProxy: MetricsProxy;
  private readonly config = require('./../config');
  private readonly statusReporterProxyService: StatusReporterProxyService;
  constructor(load_balancer_host: string,  retrySettings: RetrySettings, statusReporterProxyService: StatusReporterProxyService) {
    this._gatewayBaseUrl = `${load_balancer_host}`;
    this.retrySettings = retrySettings;
    this.metricsProxy = new MetricsProxy(retrySettings)
    this.statusReporterProxyService = statusReporterProxyService;
  }

    async getPublicKey(token: string): Promise<string> {
      try {
        const headers = {
          Authorization: `Bearer ${token}`,
        };

        const response = await axios.get(
            `${this._gatewayBaseUrl}${this._gatewayPath}applications/public-key`,
            {headers},
        );
        return response.data;
      } catch (error: any) {
        if (error.response && error.response.status === HttpStatus.NOT_FOUND) {
          console.error(`Application not found`);
          throw new ApplicationNotFound();
        } else {
          const errorMessage = `Error getting public key merchant: ${error.message}`;
          console.error(errorMessage);
          throw new Error(errorMessage);
        }
      }
    }
async authorizePaymentWithRetry(encryptedCardInfo: object, token: string): Promise<AuthorizeDto> {
    const operation = retry.operation({
      retries: this.retrySettings.retries,
      factor: this.retrySettings.factor,
      minTimeout: this.retrySettings.minTimeout,
      maxTimeout: this.retrySettings.maxTimeout,
      randomize: this.retrySettings.randomize,
    });

    let lastError: Error | undefined;
    const start = new Date().getTime();

    return new Promise<AuthorizeDto>((resolve, reject) => {
      operation.attempt(async (currentAttempt) => {
        try {
          const httpOptions: AxiosRequestConfig = {
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${token}`,
            },
          };

          await this.statusReporterProxyService.isServiceAvailable(this.config.service_authorizer_name); 

          const response = await axios.post(
            `${this._gatewayBaseUrl}${this._gatewayPath}authorize`,
            encryptedCardInfo,{
            ...httpOptions, timeout: this.retrySettings.maxTimeout,}
          );
          resolve(response.data);
          const end = new Date().getTime();
          const time = end - start;
          const request : RequestDto = new RequestDto(new Date().toISOString(), time, 'SUCCESS', 'Payment authorized');
          await this.metricsProxy.sendRequestResult(request, token);
        } catch (error: any) {
          lastError = error;
          const end = new Date().getTime();
          const time = end - start;
          if (operation.retry(lastError)) {
            console.error(`Retry attempt ${currentAttempt}, error is ${lastError?.message} failed. Retrying...`);
            return;
          }
           console.error(`All retry attempts failed. Last error: ${lastError?.message}`);

          if (isAxiosError(lastError) && lastError.response) {
            if (lastError.response.status === HttpStatus.UNAUTHORIZED) {
              reject(new UnauthorizedError(lastError.response.data.details));
              return;
            } else if (lastError.response.status === HttpStatus.NOT_FOUND) {
              reject(new ApplicationNotFound());
              return;
            } else if (lastError.response.status === HttpStatus.INTERNAL_SERVER_ERROR) {
              reject(new InternalServerError(lastError.response));
            }else if (lastError.response.status === HttpStatus.TOO_MANY_REQUESTS) {
               console.error(`the service is currently experiencing high demand.`);
            }
          } else {
            const errorMessage = `Error while processing payment: ${lastError?.message}`;
            reject(new Error(errorMessage));
          }
          const request : RequestDto = new RequestDto(new Date().toISOString(), time, 'FAILED', 'Payment authorization failed');
          await this.metricsProxy.sendRequestResult(request, token);
        }
      });
    });
  }
}

function isAxiosError(error: any): error is { isAxiosError: boolean; response: any } {
  return error.isAxiosError === true && error.response !== undefined;
}




