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

  async integrateMerchant(merchant: any): Promise<MerchantDTO> {
    try {
      const url = `${this._gatewayBaseUrl}${this._gatewayPath}integration/merchants`;
      console.log(`Integration process started for merchant`);
      const response = await axios.post<MerchantDTO>(url, merchant);
      return response.data;
    } catch (error: any) {
      if (error.response && error.response.status === HttpStatus.CONFLICT) {
        console.error(`Error integrating merchant : Merchant already exists`);
        throw new MerchantAlreadyExists();
      } else {
        const errorMessage = `Error integrating merchant. Unexpected error: ${error.message}`;
        console.error(errorMessage);
        throw new Error(errorMessage);
      }
    }
  }

  async integrateApplication(
    applicationIntegrationDto: any,
  ): Promise<ApplicationDto> {
    try {
      const url = `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications`;
      console.log(`Integration process started for business`);
      const response = await axios.post<ApplicationDto>(
        url,
        applicationIntegrationDto,
      );
      return response.data;
    } catch (error: any) {
      if (error.response && error.response.status === HttpStatus.CONFLICT) {
        console.error(`Application already exists`);
        throw new MerchantAlreadyExists();
      } else {
        const errorMessage = `Error integrating business. Unexpected error: ${error.message}`;
        console.error(errorMessage);
        throw new Error(errorMessage);
      }
    }
  }

  async getAesKey(applicationId: string): Promise<string> {
    try {
      const response = await axios.get(
        `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications/${applicationId}/aeskey`,
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

  async createApiKey(id: string): Promise<string> {
    try {
      console.log(`Generating API key process started for application ${id}`);

      const response = await axios.post<string>(
        `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications/${id}/token`,
      );

      this._apiKey = response.data;
      return response.data;
    } catch (error: any) {
      if (error.response && error.response.status === HttpStatus.NOT_FOUND) {
        console.error(`Application not found`);
        throw new ApplicationNotFound();
      } else {
        const errorMessage = `Error while generating API key: ${error.message}`;
        console.error(errorMessage);
        throw new Error(errorMessage);
      }
    }
  }

  async getPublicKey(applicationId: string): Promise<string> {
    try {
      const response = await axios.get(
        `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications/${applicationId}/publickey`,
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
