package groupB.newbankV5.customercare.controllers.dto;

import groupB.newbankV5.customercare.entities.Account;

import java.math.BigDecimal;
import java.util.List;

public class AccountDto {

    private Long id;

    private CustomerProfileDto customerProfile;
    private String IBAN;
    private String BIC;
    private BigDecimal balance;
    private BigDecimal reservedBalance;
    private SavingsAccountDto savingsAccount;

    private List<CreditCardDto> creditCards;

    public SavingsAccountDto getSavingsAccount() {
        return savingsAccount;
    }

    public void setSavingsAccount(SavingsAccountDto savingsAccount) {
        this.savingsAccount = savingsAccount;
    }

    public CustomerProfileDto getCustomerProfile() {
        return customerProfile;
    }



    public void setCustomerProfile(CustomerProfileDto customerProfile) {
        this.customerProfile = customerProfile;
    }

    public AccountDto() {
    }

    public AccountDto(Long id, CustomerProfileDto customerProfile, String IBAN, String BIC, BigDecimal balance, BigDecimal reservedBalance, List<CreditCardDto> creditCards,
                      SavingsAccountDto savingsAccount) {
        this.id = id;
        this.customerProfile = customerProfile;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.balance = balance;
        this.reservedBalance = reservedBalance;
        this.creditCards = creditCards;
        this.savingsAccount = savingsAccount;
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
        List<CreditCardDto> creditCardDto = account.getCreditCards().stream().map(CreditCardDto::creditCardFactory).toList();

        return new AccountDto(
                account.getId(),
                CustomerProfileDto.customerProfileFactory(account.getCustomerProfile()),
                account.getIBAN(),
                account.getBIC(),
                account.getBalance(),
                account.getReservedBalance()
                , creditCardDto,
                SavingsAccountDto.savingsAccountFactory(account.getSavingsAccount())
        );
    }

    public List<CreditCardDto> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardDto> creditCards) {
        this.creditCards = creditCards;
    }

    public BigDecimal getReservedBalance() {
        return reservedBalance;
    }

    public void setReservedBalance(BigDecimal reservedBalance) {
        this.reservedBalance = reservedBalance;
    }
}
