import { PaymentInfoDTO } from '../dto/payment-info.dto';
import { AuthorizeDto } from "../dto/authorise.dto";
export declare class PaymentService {
    private readonly gatewayProxyService;
    private readonly gatewayConfirmationProxyService;
    private readonly metricsReporter;
    constructor(loadBalancerHost: string, metricsPort: number);
    validateCardInfo(paymentInfo: PaymentInfoDTO): void;
    getPublicKey(token: string): Promise<string>;
    processPayment(encryptedCardInfo: string, token: string, amount: string): Promise<AuthorizeDto>;
    private encrypteCreditCard;
    authorize(paymentInfo: PaymentInfoDTO, token: string): Promise<AuthorizeDto>;
    confirmPayment(transactionId: string, token: string): Promise<any>;
    pay(paymentInfo: PaymentInfoDTO, token: string): Promise<any>;
}
