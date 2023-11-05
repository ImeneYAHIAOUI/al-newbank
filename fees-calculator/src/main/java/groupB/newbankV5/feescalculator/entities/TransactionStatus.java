package groupB.newbankV5.feescalculator.entities;

public enum TransactionStatus {
    AUTHORIZED("AUTHORIZED"),
    FEES_CALCULATED("FEES_CALCULATED"),
    SETTLED("SETTLED"),
    PENDING_SETTLEMENT("PENDING_SETTLEMENT"),
    ;

    private String authorized;
    TransactionStatus(String authorized) {
        this.authorized = authorized;
    }

    TransactionStatus() {
    }

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }
}
