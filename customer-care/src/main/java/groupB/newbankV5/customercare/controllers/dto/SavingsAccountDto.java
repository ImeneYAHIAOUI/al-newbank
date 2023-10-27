package groupB.newbankV5.customercare.controllers.dto;

import groupB.newbankV5.customercare.entities.SavingsAccount;

import java.math.BigDecimal;

public class SavingsAccountDto {
    
    private BigDecimal balance;

    public static SavingsAccountDto savingsAccountFactory(SavingsAccount savingsAccount) {
        SavingsAccountDto savingsAccountDto = new SavingsAccountDto();
        if(savingsAccount == null) {
            savingsAccountDto.setBalance(BigDecimal.valueOf(0));
        }else{
            savingsAccountDto.setBalance(savingsAccount.getBalance());
        }
        System.out.printf("savingsAccountDto: %s%n", savingsAccountDto);
        return savingsAccountDto;
    }

    public SavingsAccountDto() {
    }

    public SavingsAccountDto(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
