import {PaymentService} from "@teamb/newbank-sdk";
import {AuthorizeDto} from "@teamb/newbank-sdk/dist/sdk/dto/authorise.dto";
import {PaymentInfoDTO} from "@teamb/newbank-sdk/dist/sdk/dto/payment-info.dto";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTgwMzk1MywiaWQiOjIsIm5hbWUiOiJhcHAiLCJlbWFpbCI6Inl1dHV5LWZnQGppby5jb20iLCJ1cmwiOiJocG9wXmxyZHJ0cHBwdXl0ZXRpaCIsImRlc2NyaXB0aW9uIjoiZHlydHNyZnVoayIsImRhdGVPZklzc3VlIjoxNjk5ODAwMzUzOTU1fQ.U1HQ3zm1FSp8RG_oznMOyigklN9R8rajGsg3OCNVvQc"
    const cardNumber = "6593028794935084";
    const cvv = "145"
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