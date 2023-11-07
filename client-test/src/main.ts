import {PaymentService} from "@teamb/newbank-sdk";

async function main() {
    const loadBalancerHost = 'localhost';
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTM1MDM1OSwiaWQiOjIsIm5hbWUiOiJhcHAxIiwiZW1haWwiOiJ5dXktZnXDqHl0dWdAamlvLmNvbSIsInVybCI6Imhwb3BwcHVldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTkzNDY3NTk1Mzd9.dMB0o-N3qviv9l0rK59X9lpaCzdiYfPaxh1MJFNLVZU"
      const paymentService = new PaymentService(loadBalancerHost);
      const paymentInfo = {
        cardNumber: '6057622817803702',
        cvv: '674',
        expirationDate: '11/2025',
        amount: '50',
      };
    const response =  await paymentService.authorize(paymentInfo, token);
    const confirm = await paymentService.confirmPayment(response.transactionId, token);
    console.log(confirm);



}
main();