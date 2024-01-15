import { PaymentInfoDTO } from "@teamb/newbank-sdk";
export declare class AppService {
    private readonly newbankSdk;
    constructor();
    payment(paymentInfoDTO: PaymentInfoDTO): Promise<void>;
    pay(paymentInfoDTO: PaymentInfoDTO): void;
    authorizePayment(paymentInfoDTO: PaymentInfoDTO): void;
    confirmPayment(transactionId: string): void;
    getBackendStatus(): Promise<import("@teamb/newbank-sdk/dist/sdk/dto/backend-status.dto").BackendStatusDto>;
    getMetrics(body: any): Promise<import("@teamb/newbank-sdk/dist/sdk/dto/metrics.dto").MetricsDto[]>;
    getHello(): string;
}
