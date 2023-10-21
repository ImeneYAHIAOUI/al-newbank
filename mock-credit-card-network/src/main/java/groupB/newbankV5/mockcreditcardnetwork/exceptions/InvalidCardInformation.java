package groupB.newbankV5.mockcreditcardnetwork.exceptions;

public class InvalidCardInformation extends Throwable {
    public InvalidCardInformation(String invalidCreditCardInformation) {
    }
    public InvalidCardInformation(String message, Throwable cause) {
        super(message, cause);
    }
}
