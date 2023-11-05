package groupB.newbankV5.paymentsettlement.entities;

public enum TransactionStatus {
    AUTHORIZED("AUTHORIZED"),
    FEES_CALCULATED("FEES_CALCULATED"),
    PENDING_SETTLEMENT("PENDING_SETTLEMENT"),
    SETTLED("SETTLED"),

    FAILED("FAILED")
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
