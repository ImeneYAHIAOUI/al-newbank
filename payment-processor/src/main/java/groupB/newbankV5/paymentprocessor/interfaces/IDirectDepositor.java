package groupB.newbankV5.paymentprocessor.interfaces;

public interface IDirectDepositor {
    public boolean validateTransaction(String accountNumber, double amount);

}
