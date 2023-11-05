package groupB.newbankV5.anaytics.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AmountReceivedPerDay {
    private LocalDate date;
    private BigDecimal totalAmountReceived;
    private BigDecimal totalFees;

    public AmountReceivedPerDay(LocalDate date, BigDecimal totalAmountReceived, BigDecimal totalFees) {
        this.date = date;
        this.totalAmountReceived = totalAmountReceived;
        this.totalFees = totalFees;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getTotalAmountReceived() {
        return totalAmountReceived;
    }

    public BigDecimal getTotalFees() {
        return totalFees;
    }
}
