import {PaymentService} from "@teamb/newbank-sdk";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTQ3Mjk5OCwiaWQiOjIsIm5hbWUiOiJhcHAxIiwiZW1haWwiOiJ5dXktZnXDqHl0dWdAamlvLmNvbSIsInVybCI6Imhwb3BwcHVldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTk0NjkzOTgzMDR9.1BKNjS09JNDdCUmnff-vfUjncY4pB3WqAIlPzbFNALc"
    const paymentService = new PaymentService(loadBalancerHost);
    const paymentInfo = {
        cardNumber: '6026623757780935',
        cvv: '430',
        expirationDate: '11/2025',
        amount: '50',
    };
    const response =  await paymentService.authorize(paymentInfo, token);
    const confirm = await paymentService.confirmPayment(response.transactionId, token);
    console.log(confirm);



}
main();