package groupB.newbankV5.paymentprocessor.controllers;

import groupB.newbankV5.paymentprocessor.controllers.dto.CreditCardInformationDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.CreditCardResponseDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentResponseDto;
import groupB.newbankV5.paymentprocessor.entities.Transaction;
import groupB.newbankV5.paymentprocessor.interfaces.ITransactionProcessor;
import groupB.newbankV5.paymentprocessor.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = PaymentProcessorController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class PaymentProcessorController {
    private static final Logger log = Logger.getLogger(PaymentProcessorController.class.getName());
    public static final String BASE_URI = "/api/payment";

    private final ITransactionProcessor transactionProcessor;

    private final TransactionRepository transactionRepository;

    @Autowired
    public PaymentProcessorController(ITransactionProcessor transactionProcessor, TransactionRepository transactionRepository) {

        this.transactionProcessor = transactionProcessor;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDto> processPayment(@RequestBody PaymentDetailsDTO paymentDetails) {
        log.info("Processing payment");
        return ResponseEntity.status(HttpStatus.OK).body(transactionProcessor.authorizePayment(paymentDetails));

    }

    @PostMapping("/checkCreditCard")
    public ResponseEntity<CreditCardResponseDto> checkCreditCard(@RequestBody CreditCardInformationDto creditCardInformationDto) {
        log.info("\u001B[32mChecking credit card\u001B[0m");
        return ResponseEntity.status(HttpStatus.OK).body(transactionProcessor.validateCreditCard(creditCardInformationDto));

    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions() {
        log.info("Getting transactions");
        return ResponseEntity.status(HttpStatus.OK).body(transactionRepository.findAll());

    }

    @PostMapping("/batchSaveTransactions")
    public ResponseEntity<List<Transaction>> batchSaveTransactions(@RequestBody List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);

    }

    @PostMapping("/reserveFunds")
    public ResponseEntity<String> reserveFunds(@RequestBody Transaction transaction) {
        log.info("\u001B[32mReserving funds\u001B[0m");
        String response = transactionProcessor.reserveFunds(transaction);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }



}
