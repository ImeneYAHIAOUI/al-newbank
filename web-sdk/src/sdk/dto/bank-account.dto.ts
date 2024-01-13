
export class BankAccountDTO {
  IBAN: string;
  BIC: string;

  constructor(IBAN: string, BIC: string) {
    this.IBAN = IBAN;
    this.BIC = BIC;
  }
}
