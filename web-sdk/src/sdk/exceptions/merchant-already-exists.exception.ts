export class MerchantAlreadyExists extends Error {
  constructor(message = 'Merchant already exists') {
    super(message);
    this.name = 'Merchant already exists';
  }
}
