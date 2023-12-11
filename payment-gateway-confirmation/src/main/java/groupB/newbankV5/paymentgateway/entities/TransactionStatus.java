package groupB.newbankV5.paymentgateway.entities;

public enum TransactionStatus {
    PENDING_SETTLEMENT,
    AUTHORIZED,
    CONFIRMED,
    FEES_CALCULATED,
    SETTLED,
    FAILED
}
