package groupB.newbankV5.anaytics.entities;

public enum TransactionStatus {
    AUTHORIZED("AUTHORIZED"),
    FEES_CALCULATED("FEES_CALCULATED"),
    SETTLED("SETTLED"),

    PENDING("PENDING"),

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
