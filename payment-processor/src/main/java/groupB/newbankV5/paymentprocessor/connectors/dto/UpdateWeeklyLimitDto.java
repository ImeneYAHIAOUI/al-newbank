package groupB.newbankV5.paymentprocessor.connectors.dto;


public class UpdateWeeklyLimitDto {

    private double amount;

    public UpdateWeeklyLimitDto(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
