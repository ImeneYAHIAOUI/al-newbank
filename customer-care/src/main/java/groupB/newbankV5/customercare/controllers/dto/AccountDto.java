package groupB.newbankV5.customercare.controllers.dto;

import groupB.newbankV5.customercare.entities.Account;
import groupB.newbankV5.customercare.entities.AccountType;
import groupB.newbankV5.customercare.entities.CreditCard;
import groupB.newbankV5.customercare.entities.CustomerProfile;


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

    private BigDecimal limit;
    private BigDecimal restOfTheWeekLimit;


    private AccountType type;


    private List<CreditCardDto> creditCards;

    public AccountDto(Long id, CustomerProfileDto customerProfileDto, String iban, String bic, BigDecimal balance, AccountType type, BigDecimal weeklyPaymentLimit,BigDecimal restOfTheWeekLimit, BigDecimal reservedBalance, List<CreditCardDto> creditCardDto, SavingsAccountDto savingsAccountDto1) {
        this.id = id;
        this.customerProfile = customerProfileDto;
        this.IBAN = iban;
        this.BIC = bic;
        this.balance = balance;
        this.type = type;
        this.limit = weeklyPaymentLimit;
        this.restOfTheWeekLimit = restOfTheWeekLimit;
        this.reservedBalance = reservedBalance;
        this.creditCards = creditCardDto;
        this.savingsAccount = savingsAccountDto1;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

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
                      SavingsAccountDto savingsAccount, AccountType type, BigDecimal limit) {
        this.id = id;
        this.customerProfile = customerProfile;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.balance = balance;
        this.reservedBalance = reservedBalance;
        this.creditCards = creditCards;
        this.savingsAccount = savingsAccount;
        this.type = type;
        this.limit = limit;

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
                account.getType(),
                account.getWeekly_payment_limit(),
                account.getRestOfTheWeekLimit(),
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

    public BigDecimal getRestOfTheWeekLimit() {
        return restOfTheWeekLimit;
    }

    public void setRestOfTheWeekLimit(BigDecimal restOfTheWeekLimit) {
        this.restOfTheWeekLimit = restOfTheWeekLimit;
    }
}
