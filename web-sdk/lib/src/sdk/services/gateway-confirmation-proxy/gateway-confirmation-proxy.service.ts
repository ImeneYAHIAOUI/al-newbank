import axios, { AxiosError, AxiosRequestConfig } from 'axios';
import * as retry from 'retry';
import { HttpStatus } from '@nestjs/common';
import { MerchantDTO } from '../../dto/merchant.dto';
import { ApplicationDto } from '../../dto/application.dto';
import { MerchantAlreadyExists } from '../../exceptions/merchant-already-exists.exception';
import { ApplicationNotFound } from '../../exceptions/application-not-found.exception';
import {AuthorizeDto} from "../../dto/authorise.dto";
import { InternalServerError } from '../../exceptions/internal-server.exception';
import { UnauthorizedError } from '../../exceptions/unauthorized.exception';
import {RetrySettings} from "../Retry-settings";

export class GatewayConfirmationProxyService {
  private readonly _gatewayBaseUrl: string;
   private readonly retrySettings: RetrySettings

  private readonly _gatewayPath = '/api/gateway-confirmation';
  constructor(load_balancer_host: string,retrySettings: RetrySettings) {
    this._gatewayBaseUrl = `http://${load_balancer_host}`;
    this.retrySettings = retrySettings;
  }
 
  async confirmPayment(transactionId: string, token: string): Promise<String> {
        const operation = retry.operation({
          retries: this.retrySettings.retries,
          factor: this.retrySettings.factor,
          minTimeout: this.retrySettings.minTimeout,
          maxTimeout: this.retrySettings.maxTimeout,
          randomize: this.retrySettings.randomize,
        });
        let lastError: Error | undefined;
        return new Promise<String>((resolve, reject) => {
          operation.attempt(async (currentAttempt) => {
            try {
              const httpOptions: AxiosRequestConfig = {
                headers: {
                  'Content-Type': 'application/json',
                  Authorization: `Bearer ${token}`,
                },
              };

               const response = await axios.post(`${this._gatewayBaseUrl}${this._gatewayPath}/${transactionId}`,httpOptions,);
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
}
function isAxiosError(error: any): error is { isAxiosError: boolean; response: any } {
  return error.isAxiosError === true && error.response !== undefined;
}