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
  private _applicationId: string;

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

  async getPublicKey(): Promise<string> {
    try {
      if (!this._applicationId) {
        throw new HttpException(`Error getting public key: token is required`, HttpStatus.BAD_REQUEST);
      }
      const response = await firstValueFrom(
        this.httpService.get(
          `${this._gatewayBaseUrl}${this._gatewayPath}integration/applications/${this._applicationId}/publickey`
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

  set applicationId(value: string) {
    this._applicationId = value;
  }
}

