package groupB.newbankV5.paymentprocessor.connectors.dto;

import java.math.BigDecimal;
import java.util.List;

public class AccountDto {
    private Long id;
    private String IBAN;
    private String BIC;

    private BigDecimal balance;

    private List<CreditCardDto> creditCards;

    public AccountDto() {
    }

    public AccountDto(String IBAN, String BIC, BigDecimal balance, List<CreditCardDto> creditCards) {
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.balance = balance;
        this.creditCards = creditCards;
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
}
