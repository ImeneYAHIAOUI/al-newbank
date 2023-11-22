package groupB.newbankV5.paymentgateway.interfaces;

import groupB.newbankV5.paymentgateway.connectors.dto.ApplicationDto;
import groupB.newbankV5.paymentgateway.entities.CreditCard;
import groupB.newbankV5.paymentgateway.exceptions.ApplicationNotFoundException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public interface IRSA {

     CreditCard decryptPaymentRequestCreditCard(String encryptedData, ApplicationDto application) throws NoSuchPaddingException,
             NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ApplicationNotFoundException, InvalidKeySpecException;

     PublicKey getOrGenerateRSAPublicKey(String name) throws NoSuchAlgorithmException, ApplicationNotFoundException, InvalidKeySpecException;
}
