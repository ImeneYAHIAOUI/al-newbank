import { GatewayProxyService } from '../services/gateway-proxy/gateway-proxy.service';
import { MerchantDTO } from '../dto/merchant.dto';
import { ApplicationInfo } from '../dto/application-info.dto';
import { ApplicationDto } from '../dto/application.dto';
import { PaymentService } from './payment.service';
import {  Logger, HttpException, HttpStatus } from '@nestjs/common';
import Axios, { AxiosResponse } from 'axios';
import { MerchantAlreadyExists } from '../exceptions/merchant-already-exists.exception';
import { ApplicationAlreadyExists } from '../exceptions/application-already-exists.exception';
export class IntegrationService {
  private readonly logger = new Logger(IntegrationService.name);
  private paymentService: PaymentService;

  constructor(private readonly gatewayProxyService: GatewayProxyService) {}

async integrateMerchant(name: string, email: string): Promise<MerchantDTO> {
    const merchant = { name, email };

    return  await this.gatewayProxyService.integrateMerchant(merchant);
}


async integrateApplication(
    name: string,
    email: string,
    url: string,
    description: string,
    merchantId: string
): Promise<ApplicationDto> {
    const integratedApplication = {
        name,
        email,
        url,
        description,
        merchantId,
    };
    return await this.gatewayProxyService.integrateApplication(integratedApplication);
}



async integrateService(application: ApplicationInfo): Promise<{ id: string, apiKey: string }> {
  try {
    const merchantIntegrationResult: MerchantDTO = await this.integrateMerchant(application.name, application.email);
     if (!merchantIntegrationResult.id) {
      throw new Error('Merchant integration failed');
    }

    const applicationIntegrationResult: ApplicationDto = await this.integrateApplication(
      merchantIntegrationResult.name,
      merchantIntegrationResult.email,
      application.url,
      application.description,
      merchantIntegrationResult.id.toString()
    );
    const applicationId: string = applicationIntegrationResult.id.toString();
    this.logger.log('Application integration result: ', applicationIntegrationResult.id.toString());
    const apiKeyResult: string = await this.gatewayProxyService.createApiKey(applicationId);
    if (!apiKeyResult) {
      throw new Error('Failed to create API key');
    }

    return { id: applicationId, apiKey: apiKeyResult };
  } catch (error) {
    throw new Error(`Error in integration service: ${error.message}`);
  }
}

  //async getApiKey(applicationId: string): Promise<string> {
    //try {
      //const apiKey = await this.gatewayProxyService.getApiKey(applicationId);
      //return apiKey;
    //} catch (error) {
      //throw new Error(`Error in integration service: ${error.message}`);
    //}
  //}
}

