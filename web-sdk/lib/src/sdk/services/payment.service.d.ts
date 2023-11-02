/// <reference types="node" />
import { PaymentInfoDTO } from '../dto/payment-info.dto';
export declare class PaymentService {
    private readonly gatewayProxyService;
    constructor(loadBalancerHost: string);
    validateCardInfo(paymentInfo: PaymentInfoDTO): void;
    retrieveLocation(): Promise<string>;
    processCardInfo(paymentInfo: PaymentInfoDTO, applicationId: string, token: string): Promise<Buffer>;
}
