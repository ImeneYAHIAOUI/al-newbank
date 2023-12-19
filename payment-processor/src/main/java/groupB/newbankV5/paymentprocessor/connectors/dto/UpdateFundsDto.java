package groupB.newbankV5.paymentprocessor.connectors.dto;


public class UpdateFundsDto {

    private double amount;

    private String operation;

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public UpdateFundsDto() {}

    public UpdateFundsDto(double amount, String operation) {
        this.amount = amount;
        this.operation = operation;
    }

    public double getAmount() {
        return amount;
    }

}
