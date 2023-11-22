// main.ts
import { PaymentService } from "@teamb/newbank-sdk";
import { AuthorizeDto } from "@teamb/newbank-sdk/dist/sdk/dto/authorise.dto";
import { PaymentInfoDTO } from "@teamb/newbank-sdk/dist/sdk/dto/payment-info.dto";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const [ , ,cardNumber, cvv, expiryDate, token] = process.argv;



    // Access command line arguments

    if ( cardNumber && cvv && expiryDate) {
        const paymentService = new PaymentService(loadBalancerHost);
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
    } else {
        const paymentService = new PaymentService(loadBalancerHost);
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: "6161522542307884",
            cvv: "907",
            expirationDate: "11/2025",
            amount: '1',
        };
        let response: AuthorizeDto;
        let tokeni = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTg5OTk0NSwiaWQiOjQsIm5hbWUiOiJjb29raWVfZmFjdG9yeV9hcHBfYjQ3NmM0ODciLCJlbWFpbCI6ImNvb2tpZS5mYWN0b3J5LmFwcDYxMzlAZ21haWwuY29tIiwidXJsIjoiaHR0cDovL2Nvb2tpZV9mYWN0b3J5X2FwcF9iNDc2YzQ4Ny5jb20iLCJkZXNjcmlwdGlvbiI6IkNvb2tpZSBGYWN0b3J5IEFwcCAtIGQyNjhlZDgwIiwiZGF0ZU9mSXNzdWUiOjE2OTk4OTYzNDU1NDR9.ZEHwKzt9Yj8yzygGB9SgNiD6LbJQ-Ob8B7kgDcaN4tk";
        response = await paymentService.authorize(paymentInfo, tokeni);
        const confirm = await paymentService.confirmPayment(response.transactionId, tokeni);
        console.log(confirm);

    }
}

main();
