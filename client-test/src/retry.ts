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

    const [ , ,cardNumber, cvv, expiryDate, token,port] = process.argv;
    const newbankSdk = new NewbankSdk(token, retrySettings);
    if ( cardNumber && cvv && expiryDate) {
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: cardNumber,
            cvv: cvv,
            expirationDate: expiryDate,
            amount: '500',
        };
        let response: AuthorizeDto;
        const authorizePromises = [];
        for (let i = 0; i < 6; i++) {
                try {
                    response = await newbankSdk.authorizePayment(paymentInfo);
                    authorizePromises.push(response);

                } catch (error: any) {
                   if(error instanceof UnauthorizedError){
                      console.error('Authorization failed:', error.message);
                   }
                }
        }

       for (let i = 0; i < authorizePromises.length; i++) {
        const confirm = await newbankSdk.confirmPayment(authorizePromises[i].transactionId);
        console.log(confirm);
        }
    }
}

main();