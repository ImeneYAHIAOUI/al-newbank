import { Injectable } from '@nestjs/common';
import {NewbankSdk, RetrySettings} from "@teamb/newbank-sdk";
import {PaymentInfoDTO} from "@teamb/newbank-sdk";

@Injectable()
export class AppService {
    private readonly newbankSdk: NewbankSdk;
    constructor() {
        const retrySettings = new RetrySettings({
                                                   retries: Number(process.env.RETRIES) || 2,
                                                   factor: Number(process.env.FACTOR) || 2,
                                                   minTimeout: Number(process.env.MIN_TIMEOUT) || 1000,
                                                   maxTimeout: Number(process.env.MAX_TIMEOUT) || 3000,
                                                   randomize: Boolean(process.env.RANDOMIZE) || true,
                                               });
        const token = process.env.NEWBANK_TOKEN;
        this.newbankSdk = new NewbankSdk(token, retrySettings);
    }
    payment(paymentInfoDTO: PaymentInfoDTO): void {
        this.newbankSdk.pay(paymentInfoDTO).then(r => console.log(r));
    }

    authorizePayment(paymentInfoDTO: PaymentInfoDTO) {
         this.newbankSdk.authorizePayment(paymentInfoDTO).then(r => console.log(r));
    }

    confirmPayment(transactionId: string): void {
        this.newbankSdk.confirmPayment(transactionId).then(r => console.log(r));
    }


    getBackendStatus() {
        return this.newbankSdk.getBackendStatus();
    }

    getMetrics(body: any) {
        return this.newbankSdk.getMetrics(body);
    }

    getHello(): string {
        return 'Hello World!';
    }

}
