import { GatewayProxyService } from '../services/gateway-proxy/gateway-proxy.service';
import { MerchantDTO } from '../dto/merchant.dto';
import { ApplicationInfo } from '../dto/application-info.dto';
import { ApplicationDto } from '../dto/application.dto';
import { PaymentService } from './payment.service';

export class IntegrationService {
  private paymentService: PaymentService;

  constructor(private readonly gatewayProxyService: GatewayProxyService) {}

  async integrateMerchant(name: string, email: string): Promise<MerchantDTO> {
    const merchant = {
      name: name,
      email: email,
    };
    const merchantIntegrationResult: MerchantDTO = await this.gatewayProxyService.integrateMerchant(merchant);
    return merchantIntegrationResult;
  }

  async integrateApplication(name: string, email: string, url: string, description: string, merchantId: string): Promise<ApplicationDto> {
    const integratedApplication = {
      name: name,
      email: email,
      url: url,
      description: description,
      merchantId: merchantId,
    };
    const applicationIntegrationResult: ApplicationDto = await this.gatewayProxyService.integrateApplication(integratedApplication);
    return applicationIntegrationResult;
  }

async IntegrateService(application: ApplicationInfo): Promise<{ id: string, apiKey: string }> {
  try {
    const merchantIntegrationResult: MerchantDTO = await this.integrateMerchant(application.name, application.email);
    const applicationIntegrationResult: ApplicationDto = await this.integrateApplication(
      merchantIntegrationResult.name,
      merchantIntegrationResult.email,
      application.url,
      application.description,
      merchantIntegrationResult.id.toString()
    );
    const applicationId: string = applicationIntegrationResult.id.toString();
    const apiKeyResult: string =await  this.gatewayProxyService.createApiKey(applicationId);
    return { id: applicationId, apiKey: apiKeyResult };
  } catch (error) {
    throw new Error(`Error in integration service: ${error.message}`);
  }
}
  async getApiKey(applicationId: string): Promise<string> {
    try {
      const apiKey = await this.gatewayProxyService.getApiKey(applicationId);
      return apiKey;
    } catch (error) {
      throw new Error(`Error in integration service: ${error.message}`);
    }
  }
}

