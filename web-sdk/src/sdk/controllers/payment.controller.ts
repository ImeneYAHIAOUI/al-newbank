import { Controller, Post, Body, Headers } from '@nestjs/common';
import * as crypto from 'crypto';
import axios from 'axios';
import { TokenMockService } from '../services/token-mock.service';

@Controller('payment')
export class PaymentController {
  constructor(private readonly tokenMockService: TokenMockService) {}

  @Post()
  async processPayment(@Body() cardInfo: any, @Headers('Authorization') token: string) {
    try {
      if (!this.tokenMockService.verifyAccessToken(token)) {
        throw new Error('Invalid access token');
      }

      if (!cardInfo || !cardInfo.cardNumber || !cardInfo.expirationDate || !cardInfo.cvv) {
        throw new Error('Invalid card information');
      }

      const ipInfoResponse = await axios.get('https://ipinfo.io');
      const location = ipInfoResponse.data.loc;
      if (!location) {
        throw new Error('Unable to retrieve location information');
      }
      console.debug('Location:', location);

      const [altitude, longitude] = location.split(',');
      const payload = {
        cardNumber: cardInfo.cardNumber,
        expirationDate: cardInfo.expirationDate,
        cvv: cardInfo.cvv,
        altitude,
        longitude,
      };

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
      const encryptedCardInfo = crypto.publicEncrypt(publicKey, Buffer.from(JSON.stringify(payload)));
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


