package groupB.newbankV5.mockcreditcardnetwork.components;

import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CardGenerationRequestDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CardGenerationResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class VirtualCardGenerator {

    public CardGenerationResponseDto generateVirtualCard(CardGenerationRequestDto cardGenerationRequestDto) {
        String cardNumber = generateCardNumber();

        LocalDate expirationDate = LocalDate.now().plusYears(2);
        String expirationDateString = expirationDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));

        String cvv = generateRandomCVV();

        return new CardGenerationResponseDto(
                cardGenerationRequestDto.getCardHolderName(),
                cardNumber,
                expirationDateString,
                cvv
        );
    }

    private String generateCardNumber() {
        StringBuilder cardNumberBuilder = new StringBuilder("6");
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            cardNumberBuilder.append(random.nextInt(10));
        }
        return cardNumberBuilder.toString();
    }

    private String generateRandomCVV() {
        Random random = new Random();
        int cvv = 100 + random.nextInt(900);
        return String.valueOf(cvv);
    }
}
