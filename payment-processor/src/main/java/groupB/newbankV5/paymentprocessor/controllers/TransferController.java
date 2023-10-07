package groupB.newbankV5.paymentprocessor.controllers;

import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferResponseDto;
import groupB.newbankV5.paymentprocessor.interfaces.ITransactionProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = TransferController.BASE_URI, produces = APPLICATION_JSON_VALUE)

public class TransferController {
    private static final Logger log = Logger.getLogger(TransferController.class.getName());
    private final ITransactionProcessor transactionProcessor;
    public static final String BASE_URI = "/api/transfer";

    public TransferController(ITransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }


    @PostMapping("/process")
    public ResponseEntity<TransferResponseDto> processPayment(@RequestBody TransferDto transferDto) {
        log.info("Processing transfer");
        return ResponseEntity.status(HttpStatus.OK).body(transactionProcessor.authorizeTransfer(transferDto));

    }
}
