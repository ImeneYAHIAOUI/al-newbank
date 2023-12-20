package groupB.newbankV5.paymentprocessor.connectors.dto;

import java.util.List;

public class AccountDto {
    private Long id;
    private String IBAN;
    private String BIC;

    private double balance;

    private double limit;
    private double restOfTheWeekLimit;

    private List<CreditCardDto> creditCards;

    public AccountDto() {
    }


    public AccountDto(String IBAN, String BIC, long balance, List<CreditCardDto> creditCards, long limit) {
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.balance = balance;
        this.creditCards = creditCards;
      this.limit = limit ;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }
    public List<CreditCardDto> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardDto> creditCardDto) {
        this.creditCards = creditCardDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRestOfTheWeekLimit() {
        return restOfTheWeekLimit;
    }

    public void setRestOfTheWeekLimit(double restOfTheWeekLimit) {
        this.restOfTheWeekLimit = restOfTheWeekLimit;
    }
}
