package groupB.newbankV5.paymentprocessor.entities;

public enum TransactionStatus {
    AUTHORIZED("AUTHORIZED"),
    FEES_CALCULATED("FEES_CALCULATED"),
    SETTLED("SETTLED"),

    FAILED("FAILED"),
    PENDING_SETTLEMENT("PENDING_SETTLEMENT")
    ;

    private String authorized;
    TransactionStatus(String authorized) {
        this.authorized = authorized;
    }

    public static TransactionStatus fromString(String transactionStatus) {
        if (transactionStatus != null) {
            for (TransactionStatus b : TransactionStatus.values()) {
                if (transactionStatus.equalsIgnoreCase(b.toString())) {
                    return b;
                }
            }
        }
        return null;
    }
}
