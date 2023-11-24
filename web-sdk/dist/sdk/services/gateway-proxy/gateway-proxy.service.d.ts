import { AuthorizeDto } from "../../dto/authorise.dto";
export declare class GatewayProxyService {
    private readonly _gatewayBaseUrl;
    private readonly _gatewayPath;
    constructor(load_balancer_host: string);
    getPublicKey(token: string): Promise<string>;
    processPayment(encryptedCardInfo: object, token: string): Promise<AuthorizeDto>;
    confirmPayment(transactionId: string, token: string): Promise<String>;
}
