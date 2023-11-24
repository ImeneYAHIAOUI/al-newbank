// main.ts
import { PaymentService } from "@teamb/newbank-sdk";
import { AuthorizeDto } from "@teamb/newbank-sdk/dist/sdk/dto/authorise.dto";
import { PaymentInfoDTO } from "@teamb/newbank-sdk/dist/sdk/dto/payment-info.dto";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const [ , ,cardNumber, cvv, expiryDate, token,port] = process.argv;



    // Access command line arguments

    if ( cardNumber && cvv && expiryDate) {
        const paymentService = new PaymentService(loadBalancerHost,parseFloat(port));
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
        const paymentService = new PaymentService(loadBalancerHost,parseFloat(port));
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: "6176011619984148",
            cvv: "994",
            expirationDate: "11/2025",
            amount: '1',
        };
        let response: AuthorizeDto;
        let tokeni = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTcwMDY1ODE3MywiaWQiOjIsIm5hbWUiOiJjb29raWVfZmFjdG9yeV9hcHBfODE4MTcwZTQiLCJlbWFpbCI6ImNvb2tpZS5mYWN0b3J5LmFwcDM3ODVAZ21haWwuY29tIiwidXJsIjoiaHR0cDovL2Nvb2tpZV9mYWN0b3J5X2FwcF84MTgxNzBlNC5jb20iLCJkZXNjcmlwdGlvbiI6IkNvb2tpZSBGYWN0b3J5IEFwcCAtIDNmMGVkZDY3IiwiZGF0ZU9mSXNzdWUiOjE3MDA2NTQ1NzMyMzl9.1dANJuFn-U-g0vynRVaLcLA8glMCDo93KNrjGnkOVCU";
        response = await paymentService.authorize(paymentInfo, tokeni);
        const confirm = await paymentService.confirmPayment(response.transactionId, tokeni);
        console.log(confirm);
    }
}

main();
