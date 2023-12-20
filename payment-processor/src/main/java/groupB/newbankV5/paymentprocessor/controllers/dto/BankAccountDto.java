package groupB.newbankV5.paymentprocessor.controllers.dto;

import groupB.newbankV5.paymentprocessor.entities.BankAccount;

public class BankAccountDto {
    private String IBAN;
    private String BIC;

    public BankAccountDto(String IBAN, String BIC) {
        this.IBAN = IBAN;
        this.BIC = BIC;
    }

    public BankAccountDto() {
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    public static BankAccount toBankAccount(BankAccountDto bankAccountDto) {
        return new BankAccount(bankAccountDto.getIBAN(), bankAccountDto.getBIC());
    }
}
