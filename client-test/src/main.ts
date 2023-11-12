// main.ts
import { PaymentService } from "@teamb/newbank-sdk";
import { AuthorizeDto } from "@teamb/newbank-sdk/dist/sdk/dto/authorise.dto";
import { PaymentInfoDTO } from "@teamb/newbank-sdk/dist/sdk/dto/payment-info.dto";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const [, , clientId, cardNumber, cvv, expiryDate, token] = process.argv;


    // Access command line arguments

    if (token && cardNumber && cvv && expiryDate) {
        const paymentService = new PaymentService(loadBalancerHost);
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: cardNumber,
            cvv: cvv,
            expirationDate: expiryDate,
            amount: '1',
        };
        let response: AuthorizeDto;
        response = await paymentService.authorize(paymentInfo, token);
        const confirm = await paymentService.confirmPayment(response.transactionId, token);
        console.log(confirm);
    } else {
        console.error("Some required environment variables are not defined.");
    }
}

main();
