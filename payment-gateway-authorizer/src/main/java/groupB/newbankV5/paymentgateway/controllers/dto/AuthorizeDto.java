package groupB.newbankV5.paymentgateway.controllers.dto;

import java.util.UUID;

public class AuthorizeDto {
    private UUID transactionId;

    public AuthorizeDto(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
}
