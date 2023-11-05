package groupB.newbankV5.businessIntegrator.controllers.dto;

import groupB.newbankV5.businessIntegrator.entities.BankAccount;
import groupB.newbankV5.businessIntegrator.entities.Merchant;

public class MerchantDto {
    private Long id;
    private String name;
    private String email;
    private BankAccount bankAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }



    public static MerchantDto merchantDtoFactory(Merchant merchant) {
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setId(merchant.getId());
        merchantDto.setName(merchant.getName());
        merchantDto.setEmail(merchant.getEmail());
        merchantDto.setBankAccount(merchant.getBankAccount());
        return merchantDto;
    }
}