import { AppService } from './app.service';
import { PaymentInfoDTO } from "@teamb/newbank-sdk";
export declare class AppController {
    private readonly appService;
    constructor(appService: AppService);
    getHello(): string;
    authorizePayment(paymentInfoDTO: PaymentInfoDTO): void;
    confirmPayment(transactionId: string): void;
    payment(paymentInfoDTO: PaymentInfoDTO): Promise<void>;
    getBackendStatus(): Promise<import("@teamb/newbank-sdk/dist/sdk/dto/backend-status.dto").BackendStatusDto>;
    getMetrics(body: any): Promise<import("@teamb/newbank-sdk/dist/sdk/dto/metrics.dto").MetricsDto[]>;
}
