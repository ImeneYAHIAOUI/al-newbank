import { MerchantDTO } from '../dto/merchant.dto';
import { ApplicationDto } from '../dto/application.dto';
export declare class IntegrationService {
    private readonly gatewayService;
    constructor(loadBalancerHost: string);
    validateMerchantInfo(name: string, email: string, iban: string, bic: string): void;
    integrateMerchant(name: string, email: string, iban: string, bic: string): Promise<MerchantDTO>;
    validateApplicationInfo(name: string, email: string, url: string, description: string, merchantId: string): void;
    integrateApplication(name: string, email: string, url: string, description: string, merchantId: string): Promise<ApplicationDto>;
    integrateService(application: {
        name: string;
        email: string;
        IBAN: string;
        BIC: string;
        url: string;
        description: string;
    }): Promise<{
        id: string;
        apiKey: string;
    }>;
}
