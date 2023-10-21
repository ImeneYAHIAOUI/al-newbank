import {  IsNotEmpty } from 'class-validator';
import { BankAccountDTO } from './bank-account.dto';
import { ApplicationDto } from './application.dto';
export class MerchantDTO {
  id : number;
  name: string;
  email: string;
  bankAccount: BankAccountDTO;
  application : ApplicationDto;
}
