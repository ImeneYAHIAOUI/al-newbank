package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.entities.Application;
import groupB.newbankV5.paymentprocessor.entities.CreditCard;
import groupB.newbankV5.paymentprocessor.entities.Merchant;
import groupB.newbankV5.paymentprocessor.entities.Transaction;
import groupB.newbankV5.paymentprocessor.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentprocessor.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentprocessor.interfaces.ITransactionProcessor;
import groupB.newbankV5.paymentprocessor.repositories.ApplicationRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class Transactioner implements ITransactionProcessor {
    private final Integer FEE_RATE = 10;
    private final double FLAT_FEE = 0.03;
    private ApplicationRepository applicationRepository;

    @Autowired
    public Transactioner(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
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
    public void processPayment(String token, BigDecimal amount, CreditCard creditCard) throws InvalidTokenException, ApplicationNotFoundException {
        Application application = validateToken(token);
        Merchant merchant = application.getMerchant();

        String authToken = "";
        Transaction transaction = new Transaction(merchant, authToken, amount);
    }
}
