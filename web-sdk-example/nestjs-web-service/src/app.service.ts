import { Injectable } from '@nestjs/common';
import {NewbankSdk, RetrySettings} from "@teamb/newbank-sdk";
import {PaymentInfoDTO} from "@teamb/newbank-sdk";
import { ServiceUnavailableException } from '@teamb/newbank-sdk';

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


    async payment(paymentInfoDTO: PaymentInfoDTO): Promise<void> {
       try{ 
        const result  = await this.newbankSdk.authorizePayment(paymentInfoDTO);
        await this.newbankSdk.confirmPayment(result.transactionId);

    }catch(error : any){
        if(error instanceof ServiceUnavailableException ){
        console.log(error.message);
        const regex = /\d+$/; 
        const match = (error.message).match(regex);
        if(match){
            const timeToSleeping = parseInt(match[0], 10); 
            const start = new Date().getTime();
            const delayMilliseconds = timeToSleeping *1000; 
            while (new Date().getTime() - start < delayMilliseconds) {
            }
        }
    }else{
        console.log(error.message)
    }
    }
    }

    pay(paymentInfoDTO: PaymentInfoDTO){
        this.newbankSdk.pay(paymentInfoDTO)
        .then(r => console.log(r))
        .catch(error => {
          console.error(error.message);
        });
    }

    authorizePayment(paymentInfoDTO: PaymentInfoDTO) {
        this.newbankSdk.authorizePayment(paymentInfoDTO)
          .then(r => console.log(r))
          .catch(error => {
            console.error(error.message);
          });
      }
      
      confirmPayment(transactionId: string): void {
        this.newbankSdk.confirmPayment(transactionId)
          .then(r => console.log(r))
          .catch(error => {
            console.error(error.message);
          });
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
