export declare class GatewayConfirmationProxyService {
    private readonly _gatewayBaseUrl;
    private readonly _gatewayPath;
    constructor(gateway_confirmation_host: string);
    confirmPayment(transactionId: string, token: string): Promise<String>;
}
