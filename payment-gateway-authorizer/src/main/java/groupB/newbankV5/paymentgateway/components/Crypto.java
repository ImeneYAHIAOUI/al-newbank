package groupB.newbankV5.paymentgateway.components;

import groupB.newbankV5.paymentgateway.connectors.BusinessIntegratorProxy;
import groupB.newbankV5.paymentgateway.connectors.dto.ApplicationDto;
import groupB.newbankV5.paymentgateway.entities.ApplicationKeyPair;
import groupB.newbankV5.paymentgateway.entities.CreditCard;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.paymentgateway.exceptions.InvalidTokenException;
import groupB.newbankV5.paymentgateway.interfaces.IRSA;
import groupB.newbankV5.paymentgateway.repositories.ApplicationKeyPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class Crypto implements IRSA {

    private static final Logger log = Logger.getLogger(Crypto.class.getName());

    private ApplicationKeyPairRepository applicationKeyPairRepository;
    private BusinessIntegratorProxy businessIntegratorProxy;

    @Autowired
    public Crypto(ApplicationKeyPairRepository applicationKeyPairRepository,BusinessIntegratorProxy businessIntegratorProxy) {
        this.applicationKeyPairRepository = applicationKeyPairRepository;
        this.businessIntegratorProxy=businessIntegratorProxy;

    }

    @Override
    public PublicKey getOrGenerateRSAPublicKey(String token) throws NoSuchAlgorithmException, ApplicationNotFoundException, InvalidKeySpecException {
        ApplicationDto app;
        try {
            app = businessIntegratorProxy.validateToken(token);
        } catch (InvalidTokenException e) {
            throw new ApplicationNotFoundException("Application not found");
        }

        Optional<ApplicationKeyPair> optApplicationKeyPair =
                applicationKeyPairRepository.findByApplicationName(app.getName());
        if(optApplicationKeyPair.isEmpty()) {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            ApplicationKeyPair applicationKeyPair = new ApplicationKeyPair();
            applicationKeyPair.setId(UUID.randomUUID());
            applicationKeyPair.setApplicationName(app.getName());
            ByteBuffer privateKeyByteBuffer = ByteBuffer.wrap(pair.getPrivate().getEncoded());
            ByteBuffer publicKeyByteBuffer = ByteBuffer.wrap(pair.getPublic().getEncoded());
            applicationKeyPair.setPublicKey(publicKeyByteBuffer);
            applicationKeyPair.setPrivateKey(privateKeyByteBuffer);
            applicationKeyPairRepository.save(applicationKeyPair);
            return pair.getPublic();
        }
        ByteBuffer publicKeyByteBuffer = optApplicationKeyPair.get().getPublicKey();
        byte[] publicKeyBytes = new byte[publicKeyByteBuffer.remaining()];
        publicKeyByteBuffer.get(publicKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(publicKeySpec);
    }

    @Override
    public CreditCard decryptPaymentRequestCreditCard(String encryptedData, ApplicationDto application)
            throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, ApplicationNotFoundException, InvalidKeySpecException {
        Optional<ApplicationKeyPair> optApplicationKeyPair =
                applicationKeyPairRepository.findByApplicationName(application.getName());
        if(optApplicationKeyPair.isEmpty()) {

            throw new ApplicationNotFoundException("Application not found");

        }

        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte [] encoded = Base64.getDecoder().decode(Base64.getEncoder().encode(
                optApplicationKeyPair.get().getPrivateKey().array()
        ));
        PKCS8EncodedKeySpec keySpec1 = new PKCS8EncodedKeySpec(encoded);
        PrivateKey privateKey = kf.generatePrivate(keySpec1);

        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] encryptedMessageBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);


        String[] decryptedMessageArray = decryptedMessage.split(",");
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(decryptedMessageArray[0]);
        creditCard.setExpiryDate(decryptedMessageArray[1]);
        creditCard.setCvv(decryptedMessageArray[2]);
        return creditCard;
    }
}
