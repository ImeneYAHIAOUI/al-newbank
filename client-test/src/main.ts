// main.ts
import { PaymentService } from "@teamb/newbank-sdk";
import { AuthorizeDto } from "@teamb/newbank-sdk/dist/sdk/dto/authorise.dto";
import { PaymentInfoDTO } from "@teamb/newbank-sdk/dist/sdk/dto/payment-info.dto";
import { MetricsServer } from "@teamb/newbank-sdk/dist/sdk/services/Metrics-server";
import {RetrySettings} from "@teamb/newbank-sdk/dist/sdk/services/Retry-settings";
async function main() {
    const loadBalancerHost = 'localhost:80';
    const retrySettings = new RetrySettings({
                                              retries: 2,
                                              factor:2,
                                              minTimeout: 1000,
                                              maxTimeout: 3000,
                                              randomize: true,
                                            });
    console.log('Server has started successfddully.');

    const [ , ,cardNumber, cvv, expiryDate, token,port] = process.argv;


    if ( cardNumber && cvv && expiryDate) {

    const paymentService = new PaymentService(loadBalancerHost, retrySettings);
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: cardNumber,
            cvv: cvv,
            expirationDate: expiryDate,
            amount: '500',
        };
        let response: AuthorizeDto;
        response = await paymentService.authorize(paymentInfo, token);
        const confirm = await paymentService.confirmPayment(response.transactionId, token);
        console.log(confirm);
    }
}

main();
