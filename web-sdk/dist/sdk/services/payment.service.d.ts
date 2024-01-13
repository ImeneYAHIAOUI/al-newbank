import { PaymentInfoDTO } from '../dto/payment-info.dto';
import { AuthorizeDto } from "../dto/authorise.dto";
import { RetrySettings } from "./Retry-settings";
export declare class PaymentService {
    private readonly gatewayAuthorizationProxyService;
    private readonly gatewayConfirmationProxyService;
    private readonly statusReporterProxyService;
    private readonly config;
    constructor(retrySettings: RetrySettings);
    validateCardInfo(paymentInfo: PaymentInfoDTO): void;
    getPublicKey(token: string): Promise<string>;
    processPayment(encryptedCardInfo: string, token: string, amount: string): Promise<AuthorizeDto>;
    private encrypteCreditCard;
    authorize(paymentInfo: PaymentInfoDTO, token: string): Promise<AuthorizeDto>;
    confirmPayment(transactionId: string, token: string): Promise<String>;
    pay(paymentInfo: PaymentInfoDTO, token: string): Promise<String>;
}
