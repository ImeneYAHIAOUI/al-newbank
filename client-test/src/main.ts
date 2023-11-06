import {PaymentService} from "@teamb/newbank-sdk";

async function main() {
    const loadBalancerHost = 'localhost';
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTI3MDEyNSwiaWQiOjMsIm5hbWUiOiJhcHAxIiwiZW1haWwiOiJ5dXktZnXDqHl0dWdAamlvLmNvbSIsInVybCI6Imhwb3BwcHVldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTkyNjY1MjU2MTZ9.f72xUXAngDyR7_dfqVcSnDfifAYwYVIA770Zl8pIt9Y"
      const paymentService = new PaymentService(loadBalancerHost);
      const paymentInfo = {
        cardNumber: '6920522972946995',
        cvv: '127',
        expirationDate: '11/2025',
        amount: '50',
      };
    const response =  await paymentService.authorize(paymentInfo, token);
    const confirm = await paymentService.confirmPayment(response.transactionId, token);
    console.log(confirm);



}
main();