
export class BankAccountDTO {
  iban: string;
  bic: string;

  constructor(iban: string, bic: string) {
    this.iban = iban;
    this.bic = iban;
  }
}
