package groupB.newbankV5.paymentprocessor.connectors.dto;

import java.math.BigDecimal;

public class AccountDto {
    private String IBAN;
    private String BIC;

    private BigDecimal balance;

    public AccountDto() {
    }

    public AccountDto(String IBAN, String BIC, BigDecimal balance) {
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.balance = balance;
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
}
