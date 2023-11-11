import {PaymentService} from "@teamb/newbank-sdk";

async function main() {
    const loadBalancerHost = 'localhost:80';
    const token = process.env.TOKEN;
    const cardNumber = process.env.CARDNUMBER;
    const cvv = process.env.CVV;
    const date = process.env.DATE;
    console.log(token);
    console.log(cardNumber);
    console.log(cvv);
    console.log(date);
    const paymentService = new PaymentService(loadBalancerHost);
    const paymentInfo = {
        cardNumber: '6745178464580114',
        cvv: cvv,
        expirationDate: date,
        amount: '50',
    };
    const response =  await paymentService.authorize(paymentInfo, token);
    const confirm = await paymentService.confirmPayment(response.transactionId, token);
    console.log(confirm);



}
main();