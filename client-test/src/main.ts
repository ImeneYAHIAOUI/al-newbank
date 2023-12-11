// main.ts
import express from 'express';
import bodyParser from 'body-parser';
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
    const app = express();
    app.use(bodyParser.json());

    app.get('/', async (req, res) => {
        res.json({message: 'Hello World!'});
    });
    app.get('/backend-status', async (req, res) => {
        const backendStatus = await newbankSdk.getBackendStatus();
        res.json(backendStatus);
    });



    if ( cardNumber && cvv && expiryDate) {
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: cardNumber,
            cvv: cvv,
            expirationDate: expiryDate,
            amount: '500',
        };
        let response: AuthorizeDto;
        try {
            response = await newbankSdk.authorizePayment(paymentInfo);
            const confirm = await newbankSdk.confirmPayment(response.transactionId);
            console.log(confirm);
        } catch (error: any) {
           if(error instanceof UnauthorizedError){
              console.error('Authorization failed:', error.message);
           }
           return;
        }

    }


    const server = app.listen(port || 6906, () => {
        console.log(`Server is running on port ${port || 6906}`);
    });

    process.on('SIGINT', () => {
        server.close(() => {
            console.log('Server closed');
            process.exit(0);
        });
    });
}
main();