package groupB.newbankV5.customercare.interfaces;

import groupB.newbankV5.customercare.connectors.dto.CardGenerationRequestDto;
import groupB.newbankV5.customercare.connectors.dto.CreditCardDto;

public interface ICreditCardNetwork {
    CreditCardDto getCreditCardDetails(CardGenerationRequestDto cardGenerationRequest);
}
