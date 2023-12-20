package groupB.newbankV5.paymentgateway.interfaces;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ITransactionConfirmation {

    String confirmPayment(UUID transactionId) throws ExecutionException, InterruptedException, TimeoutException;
}
