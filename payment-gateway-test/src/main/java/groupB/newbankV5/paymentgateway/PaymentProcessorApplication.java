package groupB.newbankV5.paymentgateway;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class PaymentProcessorApplication {

	public static void main(String[] args) {

		try {

			// Decode the Base64-encoded public key
			byte[] publicKeyBytes = Base64.getDecoder().decode(
				args[0]
			);
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

			// String to be encoded
			String originalText = args[1];

			// Initialize the Cipher with the public key for encryption
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			// Encrypt the string
			byte[] encryptedBytes = cipher.doFinal(originalText.getBytes("UTF-8"));

			// Encode the encrypted bytes in Base64
			String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);

			System.out.println(encryptedBase64);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
