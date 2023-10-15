import { Injectable } from '@nestjs/common';
import { GatewayProxyService } from './gateaway-proxy/gateaway-proxy.service';
import { MerchantDTO } from '../dto/merchant.dto';
import { ApplicationInfo } from '../dto/application-info.dto';
import { ApplicationDto } from '../dto/application.dto';

@Injectable()
export class AppService {
  constructor(private readonly gatewayProxyService: GatewayProxyService) {}

  async getService(application: ApplicationInfo): Promise<string> {
    try {
      const merchant = {
        name: application.name,
        email: application.email,
      };
      const merchantIntegrationResult: MerchantDTO = await this.gatewayProxyService.integrateMerchant(merchant);

      const integratedApplication = {
        name: merchantIntegrationResult.name,
        email: merchantIntegrationResult.email,
        url: application.url,
        description: application.description,
        merchantId: merchantIntegrationResult.id,
      }

      const applicationIntegrationResult: ApplicationDto = await this.gatewayProxyService.integrateApplication(integratedApplication);
      return 'Welcome to the sdk service!';
    } catch (error) {
      throw new Error(`Error in integration service: ${error.message}`);
    }
  }
}
