package groupB.newbankV5.paymentprocessor.entities;

public enum CardType {
    CREDIT, DEBIT;

    public static CardType fromString(String cardType) {
        if (cardType != null) {
            for (CardType b : CardType.values()) {
                if (cardType.equalsIgnoreCase(b.toString())) {
                    return b;
                }
            }
        }
        return null;
    }

}
