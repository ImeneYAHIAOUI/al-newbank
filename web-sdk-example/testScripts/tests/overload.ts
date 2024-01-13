import { PaymentService } from "@teamb/newbank-sdk";
import { AuthorizeDto } from "@teamb/newbank-sdk/dist/sdk/dto/authorise.dto";
import { PaymentInfoDTO } from "@teamb/newbank-sdk/dist/sdk/dto/payment-info.dto";
import * as fs from 'fs';
import * as csvParser from 'csv-parser';

async function processRow(loadBalancerHost: string, token: string, row: any): Promise<void> {
    const { ClientID, CardNumber, CVV, ExpiryDate } = row;

    if (CardNumber && CVV && ExpiryDate) {
        const paymentService = new PaymentService(loadBalancerHost);
        const paymentInfo: PaymentInfoDTO = {
            cardNumber: CardNumber,
            cvv: CVV,
            expirationDate: ExpiryDate,
            amount: '1',
        };
        try{
        let response: AuthorizeDto;
        response = await paymentService.authorize(paymentInfo, token);
        const confirm = await paymentService.confirmPayment(response.transactionId, token);
        console.log(confirm);
        }catch(error : any){
        console.log(error);
        }
    } else {
        console.error('Invalid data in CSV row: ', row);
    }
}

async function main() {
    const loadBalancerHost = 'localhost:80';
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTg0MDI2MSwiaWQiOjE0LCJuYW1lIjoiYXBwbGNpIiwiZW1haWwiOiJ5dXR1eS1mZ0BqaW8uY29tIiwidXJsIjoiaHBvcF5sY3JkcnRwcHNwdWFheXRldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTk4MzY2NjE0Nzh9.JLZplVFME3LpC0It6JjGHiYEHgfRSXDtUnw9YNRPMG4"

    // Read data from CSV file
    const data: any[] = [];
    fs.createReadStream('./client_cards.csv')
        .pipe(csvParser())
        .on('data', (row) => {
            data.push(row);
        })
        .on('end', async () => {
            // Use Promise.all to parallelize the processing of each row
            await Promise.all(data.map((row) => processRow(loadBalancerHost, token, row)));
            console.log('All rows processed');
        });
}

main();
