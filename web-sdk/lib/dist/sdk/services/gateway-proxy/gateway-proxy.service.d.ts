import { MerchantDTO } from '../../dto/merchant.dto';
import { ApplicationDto } from '../../dto/application.dto';
export declare class GatewayProxyService {
    private readonly _gatewayBaseUrl;
    private readonly _gatewayPath;
    private _apiKey;
    constructor(load_balancer_host: string);
    integrateMerchant(merchant: any): Promise<MerchantDTO>;
    integrateApplication(applicationIntegrationDto: any): Promise<ApplicationDto>;
    getAesKey(applicationId: string): Promise<string>;
    createApiKey(id: string): Promise<string>;
    getPublicKey(applicationId: string): Promise<string>;
    processPayment(encryptedCardInfo: string): Promise<string>;
}
