import { Controller, Post, Body } from '@nestjs/common';
import * as crypto from 'crypto';

@Controller('payment')
export class PaymentController {
  @Post()
  processPayment(@Body() cardInfo: any) {
    try {
      if (!cardInfo || !cardInfo.cardNumber || !cardInfo.expirationDate || !cardInfo.cvv) {
        throw new Error('Invalid card information');
      }

      const { publicKey, privateKey } = crypto.generateKeyPairSync('rsa', {
        modulusLength: 2048,
        publicKeyEncoding: {
          type: 'spki',
          format: 'pem',
        },
        privateKeyEncoding: {
          type: 'pkcs8',
          format: 'pem',
        },
      });

      const encryptedCardInfo = crypto.publicEncrypt(publicKey, Buffer.from(JSON.stringify(cardInfo)));

      console.debug('Encrypted Card Information:', encryptedCardInfo.toString('base64'));

      const decryptedCardInfo = crypto.privateDecrypt(privateKey, encryptedCardInfo);

      const parsedCardInfo = JSON.parse(decryptedCardInfo.toString());
      console.debug('Decrypted Card Information:', parsedCardInfo);

      return { message: 'Payment received successfully!' };
    } catch (error) {
      console.error('Error processing payment:', error);
      return { message: 'Error processing payment' };
    }
  }
}

