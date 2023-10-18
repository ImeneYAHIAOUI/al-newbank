import { Injectable, Logger, HttpException, HttpStatus } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { HttpService } from '@nestjs/axios';
import { firstValueFrom } from 'rxjs';
import { MerchantDTO } from '../../dto/merchant.dto';
import { ApplicationDto } from '../../dto/application.dto';
import { DependenciesConfig } from '../../../shared/config/interfaces/dependencies-config.interface';

const logger = new Logger('GatewayProxyService');

@Injectable()
export class GatewayProxyService {
  private readonly _gatewayBaseUrl: string;
  private readonly _gatewayPath = '/api/gateway/';
  private _apiKey: string;

  constructor(
    private readonly configService: ConfigService,
    private readonly httpService: HttpService,
  ) {
    const dependenciesConfig = this.configService.get<DependenciesConfig>('dependencies');
    this._gatewayBaseUrl = `http://${dependenciesConfig.gateaway_url_with_port}`;
  }

  async integrateMerchant(merchant: any): Promise<MerchantDTO> {
    try {
      const response = await firstValueFrom(
        this.httpService.post<MerchantDTO>(
          `${this._gatewayBaseUrl}${this._gatewayPath}integration/merchants`,
          merchant,
        ),
      );
      return response.data;
    } catch (error) {
      const errorMessage = `Error integrating merchant: ${error.message}`;
      logger.error(errorMessage);
      throw new HttpException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  async getPublicKey(applicationId : string): Promise<string> {
    try {
      if (!applicationId) {
        throw new HttpException(`Error getting public key: token is required`, HttpStatus.BAD_REQUEST);
      }
      const response = await firstValueFrom(
        this.httpService.get(
          `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications/${applicationId}/publickey`
        ),
      );
      return response.data;
    } catch (error) {
      const errorMessage = `Error getting public key merchant: ${error.message}`;
      logger.error(errorMessage);
      throw new HttpException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  async integrateApplication(applicationIntegrationDto: any): Promise<ApplicationDto> {
    try {
      const response = await firstValueFrom(
        this.httpService.post<ApplicationDto>(
          `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications`,
          applicationIntegrationDto,
        ),
      );
      return response.data;
    } catch (error) {
      const errorMessage = `Error integrating application: ${error.message}`;
      logger.error(errorMessage);
      throw new HttpException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
async createApiKey(id: string): Promise<string> {
  try {
    const response = await firstValueFrom(
      this.httpService.post<string>(
        `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications/${id}/token`
      )
    );
    this._apiKey = response.data;
    return response.data;
  } catch (error) {
    const errorMessage = `Error while generating API key: ${error.message}`;
    logger.error(errorMessage);
    throw new HttpException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
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
    const errorMessage = `Error while processing payment: ${error.message}`;
    logger.error(errorMessage);
    throw new HttpException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

}

