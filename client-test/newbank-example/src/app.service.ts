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
    async payment(paymentInfoDTO: PaymentInfoDTO): Promise<void> {
       try{ 
        const result  = await this.newbankSdk.authorizePayment(paymentInfoDTO);
        await this.newbankSdk.confirmPayment(result.transactionId);

    }catch(error : any){
         console.log(error );

        console.log(${error?.message} );
           
        const start = new Date().getTime();
        const delayMilliseconds = 2000; 
        while (new Date().getTime() - start < delayMilliseconds) {
        }
    }
    }

    authorizePayment(paymentInfoDTO: PaymentInfoDTO) {
        this.newbankSdk.authorizePayment(paymentInfoDTO)
          .then(r => console.log(r))
          .catch(error => {
            console.error("An error occurred during authorization:", error);
          });
      }
      
      confirmPayment(transactionId: string): void {
        this.newbankSdk.confirmPayment(transactionId)
          .then(r => console.log(r))
          .catch(error => {
            console.error("An error occurred during confirmation:", error);
          });
      }

    async payMany(paymentInfoDTO: PaymentInfoDTO) : Promise<void> {
        let i=0;
        while(i<30){
            i++;
            try{
                await this.newbankSdk.pay(paymentInfoDTO);
            }catch(error : any){
                console.log(error)
                const start = new Date().getTime();
                const delayMilliseconds = 2000; 
                while (new Date().getTime() - start < delayMilliseconds) {
                }
            }
        }
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
