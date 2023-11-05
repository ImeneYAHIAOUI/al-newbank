export class PaymentDto {
   encryptedCard: string;
    amount: string;

    constructor(encryptedCard: string, amount: string) {
        this.encryptedCard = encryptedCard;
        this.amount = amount;
    }
}