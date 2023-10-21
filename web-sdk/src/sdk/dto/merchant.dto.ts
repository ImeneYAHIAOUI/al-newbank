import {  IsNotEmpty } from 'class-validator';
import { BankAccountDTO } from './bank-account.dto';
export class MerchantDTO {
  id : number;
  name: string;
  email: string;
  bankAccount: BankAccountDTO;
}
