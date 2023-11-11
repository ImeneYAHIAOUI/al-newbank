import {PaymentService} from "@teamb/newbank-sdk";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTcwMTUyNCwiaWQiOjIsIm5hbWUiOiJhcHAxIiwiZW1haWwiOiJ5dXktZnXDqHl0dWdAamlvLmNvbSIsInVybCI6Imhwb3BwcHVldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTk2OTc5MjQ4MTR9.r3mVK9KofUoaYsgd3ZxJdSbBMLdz5WQ0eljzKCgYLEg"
    const paymentService = new PaymentService(loadBalancerHost);
    const paymentInfo = {
        cardNumber: '6745178464580114',
        cvv: '252',
        expirationDate: '11/2025',
        amount: '50',
    };
    const response =  await paymentService.authorize(paymentInfo, token);
    const confirm = await paymentService.confirmPayment(response.transactionId, token);
    console.log(confirm);



}
main();