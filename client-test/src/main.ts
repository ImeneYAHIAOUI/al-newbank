import {PaymentService} from "@teamb/newbank-sdk";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTcyODM5NywiaWQiOjIsIm5hbWUiOiJhcHAxIiwiZW1haWwiOiJ5dXktZnXDqHl0dWdAamlvLmNvbSIsInVybCI6Imhwb3BwcHVldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTk3MjQ3OTc0Njl9.Qnblxw75RBBMt4clzejtlwvgqOrqj-gHPhzrZ0vA26s"
    const paymentService = new PaymentService(loadBalancerHost);
    const paymentInfo = {
        cardNumber: '6941730066055196',
        cvv: '624',
        expirationDate: '11/2025',
        amount: '1',
    };
    const response =  await paymentService.authorize(paymentInfo, token);
    const confirm = await paymentService.confirmPayment(response.transactionId, token);
    console.log(confirm);



}
main();