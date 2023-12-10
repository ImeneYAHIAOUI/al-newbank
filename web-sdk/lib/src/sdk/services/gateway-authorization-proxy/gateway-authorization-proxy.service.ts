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
export class GatewayAuthorizationProxyService {
  private readonly _gatewayBaseUrl: string;
  private readonly _gatewayPath = '/api/gateway_authorization/';
  private readonly retrySettings: RetrySettings
  constructor(load_balancer_host: string,  retrySettings: RetrySettings) {
    this._gatewayBaseUrl = `${load_balancer_host}`;
    this.retrySettings = retrySettings;
  }
   async getPublicKey( token: string): Promise<string> {
      try {
        const headers = {
          Authorization: `Bearer ${token}`,
        };

        const response = await axios.get(
          `${this._gatewayBaseUrl}${this._gatewayPath}applications/public-key`,
            { headers },
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

    return new Promise<AuthorizeDto>((resolve, reject) => {
      operation.attempt(async (currentAttempt) => {
        try {
          const httpOptions: AxiosRequestConfig = {
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${token}`,
            },
          };

          const response = await axios.post(
            `${this._gatewayBaseUrl}${this._gatewayPath}authorize`,
            encryptedCardInfo,
            httpOptions,
          );
          resolve(response.data);
        } catch (error: any) {
          lastError = error;
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
          if (operation.retry(lastError)) {
             console.error(`Retry attempt ${currentAttempt} failed. Retrying...`);
             return;
          }
          console.error(`All retry attempts failed. Last error: ${lastError?.message}`);
        }
      });
    });
  }
}

function isAxiosError(error: any): error is { isAxiosError: boolean; response: any } {
  return error.isAxiosError === true && error.response !== undefined;
}




