import {PaymentService} from "@teamb/newbank-sdk";
import {AuthorizeDto} from "@teamb/newbank-sdk/dist/sdk/dto/authorise.dto";
import {PaymentInfoDTO} from "@teamb/newbank-sdk/dist/sdk/dto/payment-info.dto";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTgxNzU5NywiaWQiOjIsIm5hbWUiOiJhcHAxIiwiZW1haWwiOiJ5dXktZnXDqHl0dWdAamlvLmNvbSIsInVybCI6Imhwb3BwcHVldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTk4MTM5OTc4NjB9.8yDxyBWwf-4a8Wg0ogRgcx90GdiN3aI9-YjKH1P0KV4"
    const cardNumber = "6669818518142682";
    const cvv = "250"
    const date = "11/2025";

    if (token && cardNumber && cvv && date) {
        const paymentService = new PaymentService(loadBalancerHost);
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: cardNumber,
            cvv: cvv,
            expirationDate: date,
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