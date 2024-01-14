package groupB.newbankV5.paymentgateway.controllers;

import groupB.newbankV5.paymentgateway.controllers.dto.AuthorizeDto;
import groupB.newbankV5.paymentgateway.controllers.dto.PaymentDto;
import groupB.newbankV5.paymentgateway.entities.Transaction;
import groupB.newbankV5.paymentgateway.entities.TransactionStatus;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.CCNException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentgateway.interfaces.IRSA;
import groupB.newbankV5.paymentgateway.interfaces.ITransactionFinder;
import groupB.newbankV5.paymentgateway.interfaces.ITransactionProcessor;
import groupB.newbankV5.paymentgateway.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = TransactionerController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class TransactionerController {
    private static final Logger log = Logger.getLogger(TransactionerController.class.getName());
    private static int ErrorCode=200;
    private static boolean toggle= false;
    public static final String BASE_URI = "/api/gateway_authorization";
    private final IRSA crypto;
    private final ITransactionProcessor transactionProcessor;
    private final ITransactionFinder transactionFinder;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionerController(ITransactionProcessor transactionProcessor,IRSA crypto, ITransactionFinder transactionFinder,
            TransactionRepository transactionRepository) {
        this.transactionProcessor = transactionProcessor;
        this.crypto=crypto;
        this.transactionFinder=transactionFinder;
        this.transactionRepository = transactionRepository;
    }


    @PostMapping("simulate")
    public ResponseEntity<String> activeToggle(@RequestParam int errorCode) {
        ErrorCode = errorCode;
                        toggle = errorCode != 200;
                return ResponseEntity.status(200).body("ok");
            }

            @PostMapping("authorize")
            public ResponseEntity<AuthorizeDto> processPayment(@RequestBody PaymentDto paymentDetails, @RequestHeader("Authorization") String authorizationHeader )
            throws InvalidTokenException, ApplicationNotFoundException, CCNException, NoSuchPaddingException,
                    IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException,
                    InvalidKeySpecException, ExecutionException, InterruptedException, TimeoutException {
                if(toggle){
                    try {
                        HttpStatus httpStatus = HttpStatus.valueOf(ErrorCode);
                        //save a failed transaction
                        transactionProcessor.saveFailedTransaction(authorizationHeader.substring(7),
                                paymentDetails.getAmount(), paymentDetails.getEncryptedCard());
                        return ResponseEntity.status(httpStatus).body(null);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(500).body(null);
            }
        }else {
            log.info("\u001B[32mProcessing payment request\u001B[0m");
            // Remove the "Bearer " prefix
            String token = authorizationHeader.substring(7);
            UUID transactionId = transactionProcessor.processPayment(token,
                    paymentDetails.getAmount(),
                    paymentDetails.getEncryptedCard()).getId();
            AuthorizeDto authorizeDto = new AuthorizeDto(transactionId);
            return ResponseEntity.status(200).body(authorizeDto);
        }


    }


    @GetMapping("applications/public-key")
    public ResponseEntity<String> getAesKey(HttpServletRequest request, @RequestHeader("Authorization") String authorizationHeader) throws NoSuchAlgorithmException, ApplicationNotFoundException, InvalidKeySpecException {
        String token = authorizationHeader.substring(7);
        PublicKey publicKey = crypto.getOrGenerateRSAPublicKey(token);
        return ResponseEntity.ok().body(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    }


    @GetMapping
    public ResponseEntity<List<Transaction>> findTransactions(){
        return ResponseEntity.ok().body(transactionRepository.
                findByStatus(TransactionStatus.AUTHORIZED));
    }


}
