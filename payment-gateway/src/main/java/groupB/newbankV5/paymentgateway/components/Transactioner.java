package groupB.newbankV5.paymentgateway.components;

import groupB.newbankV5.paymentgateway.config.KafkaProducerService;
import groupB.newbankV5.paymentgateway.connectors.CreditCardNetworkProxy;
import groupB.newbankV5.paymentgateway.connectors.dto.CcnResponseDto;
import groupB.newbankV5.paymentgateway.connectors.dto.PaymentDetailsDTO;
import groupB.newbankV5.paymentgateway.controllers.TransactionerController;
import groupB.newbankV5.paymentgateway.entities.Application;
import groupB.newbankV5.paymentgateway.entities.CreditCard;
import groupB.newbankV5.paymentgateway.entities.Merchant;
import groupB.newbankV5.paymentgateway.entities.Transaction;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.CCNException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentgateway.interfaces.IRSA;
import groupB.newbankV5.paymentgateway.interfaces.ITransactionProcessor;
import groupB.newbankV5.paymentgateway.repositories.ApplicationRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@Service
public class Transactioner implements ITransactionProcessor {
    private final Integer FEE_RATE = 10;
    private final double FLAT_FEE = 0.03;
    private static final Logger log = Logger.getLogger(Transactioner.class.getName());

    private ApplicationRepository applicationRepository;
    private CreditCardNetworkProxy creditCardNetworkProxy;
    private IRSA rsa;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    public Transactioner(ApplicationRepository applicationRepository,
                         CreditCardNetworkProxy creditCardNetworkProxy, IRSA rsa) {
        this.applicationRepository = applicationRepository;
        this.creditCardNetworkProxy = creditCardNetworkProxy;
        this.rsa = rsa;
    }

    @Override
    public Application validateToken(String token) throws InvalidTokenException, ApplicationNotFoundException {
        Jws<Claims> jws;
        try {
            jws = Jwts.parser().setSigningKey(Integrator.SECRET_KEY).parseClaimsJws(token);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token");
        }
        Claims bodyClaims = jws.getBody();
        Long applicationId = bodyClaims.get("id", Long.class);
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application with id " + applicationId + " not found"));
    }

    @Override
    public void processPayment(String token, BigDecimal amount, String cryptedCreditCard) throws InvalidTokenException,
            ApplicationNotFoundException, CCNException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        Application application = validateToken(token);
        log.info("token validated");
        Merchant merchant = application.getMerchant();
        log.info("Decrypted credit card: "+ cryptedCreditCard);
        CreditCard creditCard = rsa.decryptPaymentRequestCreditCard(cryptedCreditCard, application);
        log.info("successfully decrypted credit card");

        CcnResponseDto ccnResponseDto = creditCardNetworkProxy.authorizePayment(
                new PaymentDetailsDTO(creditCard.getCardNumber(), creditCard.getExpiryDate(), creditCard.getCvv())
        );
        if (!ccnResponseDto.isApproved()) {
            throw new CCNException("Payment not authorized");
        }
        Transaction transaction = new Transaction(merchant.getBankAccount(), ccnResponseDto.getAuthToken(), amount);
        transaction.setExternal(true);

        kafkaProducerService.sendMessage(transaction);
    }
}
