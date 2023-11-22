package groupB.newbankV5.paymentgateway.exceptions;

public class CCNException extends Exception {
    public CCNException(String paymentNotAuthorized) {
        super(paymentNotAuthorized);
    }
}
