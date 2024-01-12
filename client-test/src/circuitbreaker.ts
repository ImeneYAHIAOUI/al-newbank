// main.ts
import {NewbankSdk, RetrySettings} from "@teamb/newbank-sdk";
import {PaymentInfoDTO} from "@teamb/newbank-sdk";
import {AuthorizeDto} from "@teamb/newbank-sdk";
import {UnauthorizedError} from "@teamb/newbank-sdk";
import {ServiceUnavailableException} from "@teamb/newbank-sdk";
import bodyParser from "body-parser";
import express from "express";

async function main() {
    const retrySettings = new RetrySettings({
                                              retries: 2,
                                              factor:2,
                                              minTimeout: 1000,
                                              maxTimeout: 3000,
                                              randomize: true,
                                            });

    const responseTimeout= 5000;
    const app = express();
    app.use(bodyParser.json());
    app.get('/', async (req, res) => {
        res.json({message: 'Hello World!'});
    });
    app.get('/backend-status', async (req, res) => {
        const backendStatus = await newbankSdk.getBackendStatus();
        res.json(backendStatus);
    });
    app.post('/metrics', async (req, res) => {
        const metrics = await newbankSdk.getMetrics(req.body);
        res.json(metrics);
    });
    const [ , ,cardNumber, cvv, expiryDate, token,port] = process.argv;
    const newbankSdk = new NewbankSdk(token, retrySettings);


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
                            console.error(error.getResponse());
                              const start = new Date().getTime();
                              const delayMilliseconds = 5 * 1000; // Convert seconds to milliseconds
                              while (new Date().getTime() - start < delayMilliseconds) {
                                
                              }
                            } 
                           else {
                            console.error(error);
                          }
                    }
        }
        return;

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
function sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
main();