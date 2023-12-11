package groupB.newbankV5.metrics.entities;

public enum TransactionStatus {
    AUTHORIZED("AUTHORIZED"),
    FEES_CALCULATED("FEES_CALCULATED"),
    SETTLED("SETTLED"),

    CONFIRMED("CONFIRMED"),

    PENDING_SETTLEMENT("PENDING_SETTLEMENT"),

    FAILED("FAILED")
    ;

    private String value;
    TransactionStatus(String value) {
        this.value = value;
    }

    TransactionStatus() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
