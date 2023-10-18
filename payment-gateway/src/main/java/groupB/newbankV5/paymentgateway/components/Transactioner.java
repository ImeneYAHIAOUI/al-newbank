package groupB.newbankV5.paymentgateway.components;

import groupB.newbankV5.paymentgateway.connectors.CreditCardNetworkProxy;
import groupB.newbankV5.paymentgateway.connectors.dto.CcnResponseDto;
import groupB.newbankV5.paymentgateway.connectors.dto.PaymentDetailsDTO;
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
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class Transactioner implements ITransactionProcessor {
    private final Integer FEE_RATE = 10;
    private final double FLAT_FEE = 0.03;
    private ApplicationRepository applicationRepository;
    private CreditCardNetworkProxy creditCardNetworkProxy;
    private IRSA rsa;

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
    public BigDecimal feesCalculator(Transaction transaction) {
        return transaction.getAmount().compareTo(BigDecimal.valueOf(100)) > 0 ?
                transaction.getAmount().multiply(BigDecimal.valueOf(FLAT_FEE)).add(BigDecimal.valueOf(FEE_RATE)) :
                transaction.getAmount().multiply(BigDecimal.valueOf(FLAT_FEE));
    }

    @Override
    public void processPayment(String token, BigDecimal amount, String cryptedCreditCard) throws InvalidTokenException,
            ApplicationNotFoundException, CCNException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        Application application = validateToken(token);
        Merchant merchant = application.getMerchant();
        CreditCard creditCard = rsa.decryptPaymentRequestCreditCard(cryptedCreditCard, application);
        //System.out.printf("Credit card number %s, expiry date %s, cvv %s%n", creditCard.getCardNumber(), creditCard.getExpiryDate(), creditCard.getCvv());
        CcnResponseDto ccnResponseDto = creditCardNetworkProxy.authorizePayment(
                new PaymentDetailsDTO(creditCard.getCardNumber(), creditCard.getExpiryDate(), creditCard.getCvv())
        );
        if (!ccnResponseDto.isApproved()) {
            throw new CCNException("Payment not authorized");
        }
        Transaction transaction = new Transaction(merchant, ccnResponseDto.getAuthToken(), amount);
        // TODO : Publish transaction to queue to be saved
    }

    @Override
    public void settlePayment(Transaction transaction) {
        // TODO : Add fees to transaction and publish to queue to be saved as well as request settlement from bank
    }
}
