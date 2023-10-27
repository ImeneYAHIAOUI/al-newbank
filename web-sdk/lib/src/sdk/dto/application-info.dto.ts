export class ApplicationInfo {
  name: string;
  email: string;
  IBAN: string;
  BIC: string;
  url: string;
  description: string;

  constructor(
    name: string,
    email: string,
    IBAN: string,
    BIC: string,
    url: string,
    description: string,
  ) {
    this.name = name;
    this.email = email;
    this.IBAN = IBAN;
    this.BIC = BIC;
    this.url = url;
    this.description = description;
  }
}
