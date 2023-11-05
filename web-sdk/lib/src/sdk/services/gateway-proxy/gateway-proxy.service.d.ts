export declare class GatewayProxyService {
    private readonly _gatewayBaseUrl;
    private readonly _gatewayPath;
    private _apiKey;
    constructor(load_balancer_host: string);
    getPublicKey(applicationId: string): Promise<string>;
    processPayment(encryptedCardInfo: string): Promise<string>;
}
