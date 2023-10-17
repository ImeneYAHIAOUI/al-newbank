import { GatewayProxyService } from '../services/gateway-proxy/gateway-proxy.service';
import { MerchantDTO } from '../dto/merchant.dto';
import { ApplicationInfo } from '../dto/application-info.dto';
import { ApplicationDto } from '../dto/application.dto';
import { PaymentService } from './payment.service';

export class AppService {
  private paymentService: PaymentService;

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
      };

      const applicationIntegrationResult: ApplicationDto = await this.gatewayProxyService.integrateApplication(integratedApplication);

      const applicationId = applicationIntegrationResult.id.toString();
      this.gatewayProxyService.createApiKey(applicationId);

      this.paymentService = new PaymentService(this.gatewayProxyService);
      this.paymentService.applicationId = applicationId;

      return 'Bienvenue dans le service SDK !';
    } catch (error) {
      throw new Error(`Erreur dans le service d'intégration : ${error.message}`);
    }
  }
}

