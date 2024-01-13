import { BankAccountDTO } from './bank-account.dto';
import { ApplicationDto } from './application.dto';
export class MerchantDTO {
  id: number;
  name: string;
  email: string;
  bankAccount: BankAccountDTO;
  application: ApplicationDto;

  constructor(
    id: number,
    name: string,
    email: string,
    bankAccount: BankAccountDTO,
    application: ApplicationDto,
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.bankAccount = bankAccount;
    this.application = application;
  }
}
