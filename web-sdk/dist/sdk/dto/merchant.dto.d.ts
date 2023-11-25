import { BankAccountDTO } from './bank-account.dto';
import { ApplicationDto } from './application.dto';
export declare class MerchantDTO {
    id: number;
    name: string;
    email: string;
    bankAccount: BankAccountDTO;
    application: ApplicationDto;
    constructor(id: number, name: string, email: string, bankAccount: BankAccountDTO, application: ApplicationDto);
}
