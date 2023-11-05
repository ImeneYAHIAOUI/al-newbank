package groupB.newbankV5.anaytics.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Income {
    BigDecimal amount;
    LocalDate date;

    public Income(BigDecimal amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
