import { Injectable, Logger, HttpException, HttpStatus } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { HttpService } from '@nestjs/axios';
import { firstValueFrom } from 'rxjs';
import { MerchantDTO } from '../../dto/merchant.dto';
import { ApplicationDto } from '../../dto/application.dto';
import { DependenciesConfig } from '../../../shared/config/interfaces/dependencies-config.interface';
import Axios, { AxiosResponse } from 'axios';
import { MerchantAlreadyExists } from '../../exceptions/merchant-already-exists.exception';
import { ApplicationNotFound } from '../../exceptions/application-not-found.exception';
@Injectable()
export class GatewayProxyService {
  private readonly logger = new Logger(GatewayProxyService.name);
  private readonly _gatewayBaseUrl: string;
  private readonly _gatewayPath = '/api/gateway/';
  private _apiKey: string;

  constructor(
    private readonly configService: ConfigService,
    private readonly httpService: HttpService,
  ) {
    const dependenciesConfig = this.configService.get<DependenciesConfig>('dependencies');
    this._gatewayBaseUrl = `http://${dependenciesConfig.load_balancer_url}`;
  }

async integrateMerchant(merchant: any): Promise<MerchantDTO> {
    try {
        const url = `${this._gatewayBaseUrl}${this._gatewayPath}integration/merchants`;
        this.logger.log(`Integration process started for merchant`);
        const response = await firstValueFrom(this.httpService.post<MerchantDTO>(url, merchant));
        return response.data;
    } catch (error) {
        if (error.response && error.response.status === HttpStatus.CONFLICT) {
            this.logger.error(`Error integrating merchant : Merchant already exists`);
            throw new MerchantAlreadyExists();
        } else {
            const errorMessage = `Error integrating merchant. Unexpected error: ${error.message}`;
            this.logger.error(errorMessage);
            throw new Error(errorMessage);
        }
    }
}

  async integrateApplication(applicationIntegrationDto: any): Promise<ApplicationDto> {
    try {
      const url = `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications`;
      this.logger.log(`Integration process started for business`);
      const response = await firstValueFrom(this.httpService.post<ApplicationDto>(url,applicationIntegrationDto,),);
      return response.data;
    } catch (error) {
         if (error.response && error.response.status === HttpStatus.CONFLICT) {
                 this.logger.error(`Application already exists`);
                 throw new MerchantAlreadyExists();
             } else {
                 const errorMessage = `Error integrating business. Unexpected error: ${error.message}`;
                 this.logger.error(errorMessage);
                 throw new Error(errorMessage);
             }
    }
  }

  async getPublicKey(applicationId : string): Promise<string> {
    try {
      const response = await firstValueFrom(
        this.httpService.get(
          `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications/${applicationId}/publickey`
        ),
      );
      return response.data;
    } catch (error) {
    if (error.response && error.response.status === HttpStatus.NOT_FOUND) {
          this.logger.error(`Application not found`);
          throw new ApplicationNotFound();
        } else {
      const errorMessage = `Error getting public key merchant: ${error.message}`;
      this.logger.error(errorMessage);
      throw new Error(errorMessage);
    }}
  }


async createApiKey(id: string): Promise<string> {
  try {
    this.logger.log(`Generating API key process started for application ${id}`);

    const response = await firstValueFrom(
      this.httpService.post<string>(
        `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications/${id}/token`
      )
    );

    this._apiKey = response.data;
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === HttpStatus.NOT_FOUND) {
      this.logger.error(`Application not found`);
      throw new ApplicationNotFound();
    } else {
      const errorMessage = `Error while generating API key: ${error.message}`;
      this.logger.error(errorMessage);
      throw new Error(errorMessage);
    }
  }
}

async processPayment( encryptedCardInfo: string): Promise<string> {
  try {
    const response = await firstValueFrom(
      this.httpService.post<string>(
        `${this._gatewayBaseUrl}${this._gatewayPath}/authorize`,
        encryptedCardInfo
      )
    );
    return response.data;
  } catch (error) {
  if (error.response && error.response.status === HttpStatus.NOT_FOUND) {
        this.logger.error(`Application not found`);
        throw new ApplicationNotFound();
      } else {
    const errorMessage = `Error while processing payment: ${error.message}`;
    this.logger.error(errorMessage);
    throw new HttpException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
  }}
}

}

