package groupB.newbankV5.anaytics.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MerchantAnalytics {
    private LocalDate date;
    private BigDecimal totalAmountReceived;
    private BigDecimal totalFees;
    private BigDecimal percentageProfitVariation;


    public MerchantAnalytics(LocalDate date, BigDecimal totalAmountReceived, BigDecimal totalFees) {
        this.date = date;
        this.totalAmountReceived = totalAmountReceived;
        this.totalFees = totalFees;
    }

    public MerchantAnalytics(){

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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTotalAmountReceived(BigDecimal totalAmountReceived) {
        this.totalAmountReceived = totalAmountReceived;
    }

    public void setTotalFees(BigDecimal totalFees) {
        this.totalFees = totalFees;
    }

    public BigDecimal getPercentageProfitVariation() {
        return percentageProfitVariation;
    }

    public void setPercentageProfitVariation(BigDecimal percentageProfitVariation) {
        this.percentageProfitVariation = percentageProfitVariation;
    }

    @Override
    public String toString() {
        return "AmountReceivedPerDay{" + "date=" + date + ", totalAmountReceived=" + totalAmountReceived + ", totalFees=" + totalFees + ", percentageProfitVariation=" + percentageProfitVariation + '}';
    }
}
