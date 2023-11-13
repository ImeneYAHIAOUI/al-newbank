// main.ts
import { PaymentService } from "@teamb/newbank-sdk";
import { AuthorizeDto } from "@teamb/newbank-sdk/dist/sdk/dto/authorise.dto";
import { PaymentInfoDTO } from "@teamb/newbank-sdk/dist/sdk/dto/payment-info.dto";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const [, , clientId, cardNumber, cvv, expiryDate, token] = process.argv;


    // Access command line arguments

    if ( cardNumber && cvv && expiryDate) {
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
        const paymentService = new PaymentService(loadBalancerHost);
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: "6256783866900738",
            cvv: "256",
            expirationDate: "11/2025",
            amount: '1',
        };
        let response: AuthorizeDto;
        response = await paymentService.authorize(paymentInfo, "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTgzNTQyNywiaWQiOjQsIm5hbWUiOiJhcHAxIiwiZW1haWwiOiJ5dXktZnXDqHl0dWdAamlvLmNvbSIsInVybCI6Imhwb3BwcHVldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTk4MzE4Mjc1NDF9.driNV0CGkDVigY1TG3sjaqObXqmdol5nFdTiFusK3ZE");
        const confirm = await paymentService.confirmPayment(response.transactionId, "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTgzNTQyNywiaWQiOjQsIm5hbWUiOiJhcHAxIiwiZW1haWwiOiJ5dXktZnXDqHl0dWdAamlvLmNvbSIsInVybCI6Imhwb3BwcHVldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTk4MzE4Mjc1NDF9.driNV0CGkDVigY1TG3sjaqObXqmdol5nFdTiFusK3ZE");
        console.log(confirm);

    }
}

main();
