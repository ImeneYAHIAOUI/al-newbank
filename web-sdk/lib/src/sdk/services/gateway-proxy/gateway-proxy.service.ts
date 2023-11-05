import axios, { AxiosRequestConfig } from 'axios';
import { HttpStatus } from '@nestjs/common';
import { MerchantDTO } from '../../dto/merchant.dto';
import { ApplicationDto } from '../../dto/application.dto';
import { MerchantAlreadyExists } from '../../exceptions/merchant-already-exists.exception';
import { ApplicationNotFound } from '../../exceptions/application-not-found.exception';
export class GatewayProxyService {
  private readonly _gatewayBaseUrl: string;
  private readonly _gatewayPath = '/api/gateway/';
  private _apiKey: string | undefined;
  constructor(load_balancer_host: string) {
    this._gatewayBaseUrl = `http://${load_balancer_host}`;
  }
   async getPublicKey(applicationId: string): Promise<string> {
      try {
        const response = await axios.get(
          `${this._gatewayBaseUrl}${this._gatewayPath}/applications/${applicationId}/publickey`,
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
  async processPayment(encryptedCardInfo: string): Promise<string> {
    try {
      const httpOptions: AxiosRequestConfig = {
        headers: {
          'Content-Type': 'application/json',
        },
      };
      const response = await axios.post<string>(
        `${this._gatewayBaseUrl}${this._gatewayPath}authorize`,
        encryptedCardInfo,
        httpOptions,
      );

      return response.data;
    } catch (error: any) {
      if (error.response && error.response.status === HttpStatus.NOT_FOUND) {
        console.error(`Application not found`);
        throw new ApplicationNotFound();
      } else {
        const errorMessage = `Error while processing payment: ${error.message}`;
        console.error(errorMessage);
        throw new Error(errorMessage);
      }
    }
  }
}
