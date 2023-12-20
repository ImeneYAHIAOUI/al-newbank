package groupB.newbankV5.transactions.entities;

public enum TransactionStatus {
    AUTHORIZED("AUTHORIZED"),
    FEES_CALCULATED("FEES_CALCULATED"),
    SETTLED("SETTLED"),

    PENDING_SETTLEMENT("PENDING_SETTLEMENT"),

    FAILED("FAILED")
    ;

    private String authorized;
    TransactionStatus(String authorized) {
        this.authorized = authorized;
    }

    TransactionStatus() {
    }

    public String getValue() {
        return authorized;
    }

    public void setValue(String authorized) {
        this.authorized = authorized;
    }
}
