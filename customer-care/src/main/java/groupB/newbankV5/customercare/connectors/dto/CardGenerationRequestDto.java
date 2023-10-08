package groupB.newbankV5.customercare.connectors.dto;

public class CardGenerationRequestDto {

    private String cardHolderName;

    public CardGenerationRequestDto() {
    }

    public CardGenerationRequestDto(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
}
