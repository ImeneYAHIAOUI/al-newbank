package groupB.newbankV5.feescalculator.exceptions;

public class CCNException extends Exception {
    public CCNException(String paymentNotAuthorized) {
        super(paymentNotAuthorized);
    }
}
