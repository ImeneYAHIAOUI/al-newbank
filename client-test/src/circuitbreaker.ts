// main.ts
import {NewbankSdk, RetrySettings} from "@teamb/newbank-sdk";
import {PaymentInfoDTO} from "@teamb/newbank-sdk";
import {AuthorizeDto} from "@teamb/newbank-sdk";
import {UnauthorizedError} from "@teamb/newbank-sdk";
import {ServiceUnavailableException} from "@teamb/newbank-sdk";

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

    const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));


    if ( cardNumber && cvv && expiryDate) {
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: cardNumber,
            cvv: cvv,
            expirationDate: expiryDate,
            amount: '1',
        };
        let responses: AuthorizeDto[] = [];
        let i=0;
        while(i<20){
            i++;

            try {
                           const response = await newbankSdk.authorizePayment(paymentInfo);   
                        await newbankSdk.confirmPayment(response.transactionId)                                
                    } catch (error: any) {
                        if (error instanceof ServiceUnavailableException) {
                            const response = error.getResponse();
                            if (response && typeof response === 'object' && 'message' in response && 'headers' in response) {
                              console.log('Error Message:', response.message);
                              console.log('Retry Time:', response.headers?.['Retry-After']); 
                            }
                        } else {
                            console.log(error.getResponse());
                        }
                    
                    }
        }
        return;

    }


}
function sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
main();