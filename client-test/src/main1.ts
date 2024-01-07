// main.ts
import {NewbankSdk, RetrySettings} from "@teamb/newbank-sdk";
import {PaymentInfoDTO} from "@teamb/newbank-sdk";
import {AuthorizeDto} from "@teamb/newbank-sdk";
import {UnauthorizedError} from "@teamb/newbank-sdk";

async function main() {
    const retrySettings = new RetrySettings({
                                              retries: 2,
                                              factor:2,
                                              minTimeout: 1000,
                                              maxTimeout: 3000,
                                              randomize: true,
                                            });

    const responseTimeout= 5000;
    const [ , ,cardNumber, cvv, expiryDate, token,port] = process.argv;
    const newbankSdk = new NewbankSdk(token, retrySettings);


    if ( cardNumber && cvv && expiryDate) {
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: cardNumber,
            cvv: cvv,
            expirationDate: expiryDate,
            amount: '500',
        };
        let responses: AuthorizeDto[] = [];
        let i=0;
        while(i<7){
            i++;

            try {
                if(i==6){
                    await sleep(200);
                }

                if(i==5){
                    await sleep(4900);

                }     
                        const response = await newbankSdk.authorizePayment(paymentInfo);
                         
                        if(i>=6) {
                            newbankSdk.confirmPayment(response.transactionId)
                        }
                      
                    } catch (error: any) {
                       console.log(error.message);
                    }
        }
        return;

    }


}
function sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
main();