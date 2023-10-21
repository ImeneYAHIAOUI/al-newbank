package groupB.newbankV5.paymentgateway.components;

import groupB.newbankV5.paymentgateway.entities.Application;
import groupB.newbankV5.paymentgateway.entities.ApplicationAESKey;
import groupB.newbankV5.paymentgateway.entities.CreditCard;
import groupB.newbankV5.paymentgateway.interfaces.IRSA;
import groupB.newbankV5.paymentgateway.repositories.ApplicationAESKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class Crypto implements IRSA {

    private static final Logger log = Logger.getLogger(Crypto.class.getName());

    private ApplicationAESKeyRepository applicationAESKeyRepository;

    @Autowired
    public Crypto(ApplicationAESKeyRepository applicationAESKeyRepository) {
        this.applicationAESKeyRepository = applicationAESKeyRepository;
    }

    @Override
    public SecretKey getOrGenerateAESKey(Application application) throws NoSuchAlgorithmException {
        Optional<ApplicationAESKey> optApplicationAESKey = applicationAESKeyRepository.findByApplication(application);
        if (optApplicationAESKey.isEmpty()) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey aesKey = keyGenerator.generateKey();

            ApplicationAESKey applicationAESKey = new ApplicationAESKey();
            applicationAESKey.setApplication(application);
            applicationAESKey.setAesKey(aesKey.getEncoded());
            applicationAESKeyRepository.saveAndFlush(applicationAESKey);
            log.info("AES Key generated for application " + aesKey.toString());
            return aesKey;
        }

        byte[] aesKeyBytes = optApplicationAESKey.get().getAesKey();
        log.info("AES Key generated for application " + new SecretKeySpec(aesKeyBytes, "AES").toString());
        return new SecretKeySpec(aesKeyBytes, "AES");
    }

    @Override
    public String encryptCreditCard(CreditCard creditCard, SecretKey aesKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);

        String plainText = creditCard.getCardNumber() + "," + creditCard.getExpiryDate() + "," + creditCard.getCvv();
        byte[] encryptedData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    @Override
    public CreditCard decryptPaymentRequestCreditCard(String encryptedData, Application application ) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Optional<ApplicationAESKey> optApplicationAESKey = applicationAESKeyRepository.findByApplication(application);
        log.info("aes key: " + new SecretKeySpec(optApplicationAESKey.get().getAesKey(), "AES").toString());
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(optApplicationAESKey.get().getAesKey(), "AES"));

        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(encryptedDataBytes);
        String decryptedMessage = new String(decryptedData, StandardCharsets.UTF_8);
        String[] decryptedMessageArray = decryptedMessage.split(",");

        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(decryptedMessageArray[0]);
        creditCard.setExpiryDate(decryptedMessageArray[1]);
        creditCard.setCvv(decryptedMessageArray[2]);

        return creditCard;
    }
}
