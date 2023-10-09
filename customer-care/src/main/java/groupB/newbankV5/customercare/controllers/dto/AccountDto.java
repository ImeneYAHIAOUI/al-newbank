package groupB.newbankV5.customercare.controllers.dto;

import groupB.newbankV5.customercare.entities.Account;
import groupB.newbankV5.customercare.entities.CreditCard;
import groupB.newbankV5.customercare.entities.CustomerProfile;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

public class AccountDto {

    private Long id;

    private CustomerProfileDto customerProfile;
    private String IBAN;
    private String BIC;
    private BigDecimal balance;

    private CreditCardDto creditCard;

    public CustomerProfileDto getCustomerProfile() {
        return customerProfile;
    }

    public CreditCardDto getCreditCard() {
        return creditCard;
    }

    public void setCreditCards(CreditCardDto creditCard) {
        this.creditCard = creditCard;
    }

    public void setCustomerProfile(CustomerProfileDto customerProfile) {
        this.customerProfile = customerProfile;
    }

    public AccountDto() {
    }

    public AccountDto(Long id, CustomerProfileDto customerProfile, String IBAN, String BIC, BigDecimal balance, CreditCardDto creditCards) {
        this.id = id;
        this.customerProfile = customerProfile;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.balance = balance;
        this.creditCard = creditCards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public static AccountDto accountDtoFactory(Account account) {
        CreditCardDto creditCardDto = null;
        if (account.getCreditCard() != null) {
            creditCardDto = CreditCardDto.creditCardFactory(account.getCreditCard());
        }
        return new AccountDto(
                account.getId(),
                CustomerProfileDto.customerProfileFactory(account.getCustomerProfile()),
                account.getIBAN(),
                account.getBIC(),
                account.getBalance(),
                creditCardDto
        );
    }
}
