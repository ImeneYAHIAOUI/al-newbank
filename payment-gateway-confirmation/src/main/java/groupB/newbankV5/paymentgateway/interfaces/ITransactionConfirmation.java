package groupB.newbankV5.paymentgateway.interfaces;

import java.util.UUID;

public interface ITransactionConfirmation {

    String confirmPayment(UUID transactionId);
}
