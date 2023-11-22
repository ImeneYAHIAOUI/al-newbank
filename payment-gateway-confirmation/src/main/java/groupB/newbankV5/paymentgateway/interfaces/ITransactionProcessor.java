package groupB.newbankV5.paymentgateway.interfaces;

import java.util.UUID;

public interface ITransactionProcessor {

    String confirmPayment(UUID transactionId);
}
