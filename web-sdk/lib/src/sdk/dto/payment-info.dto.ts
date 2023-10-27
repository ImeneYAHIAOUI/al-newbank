export class PaymentInfoDTO {
  cardNumber: string;
  cvv: string;
  expirationDate: string;
  amount: string;

  constructor(
    cardNumber: string,
    cvv: string,
    expirationDate: string,
    amount: string,
  ) {
    this.cardNumber = cardNumber;
    this.cvv = cvv;
    this.expirationDate = expirationDate;
    this.amount = amount;
  }
}
