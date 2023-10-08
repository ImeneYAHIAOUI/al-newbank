package groupB.newbankV5.mockcreditcardnetwork.controllers;
import groupB.newbankV5.mockcreditcardnetwork.components.VirtualCardGenerator;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CardGenerationRequestDto;
import groupB.newbankV5.mockcreditcardnetwork.controllers.dto.CardGenerationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = VirtualCardController.BASE_URI, produces = APPLICATION_JSON_VALUE)

public class VirtualCardController {

        public static final String BASE_URI = "/api/virtualcard";

        private final VirtualCardGenerator virtualCardGenerator;

        @Autowired
        public VirtualCardController(VirtualCardGenerator virtualCardGenerator) {
            this.virtualCardGenerator = virtualCardGenerator;
        }

        @PostMapping("/generate")
        public ResponseEntity<CardGenerationResponseDto> generateNewBankVirtualCard(@RequestBody CardGenerationRequestDto cardGenerationRequestDto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(virtualCardGenerator.generateVirtualCard(cardGenerationRequestDto));
        }
}
