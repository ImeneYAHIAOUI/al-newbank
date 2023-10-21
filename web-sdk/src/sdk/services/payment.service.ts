import { Logger, HttpException, HttpStatus } from '@nestjs/common';
import { GatewayProxyService } from '../services/gateway-proxy/gateway-proxy.service';
import { PaymentInfoDTO } from '../dto/payment-info.dto';
import { InvalidTokenException } from '../exceptions/invalid-token.exception';
import axios from 'axios';
import * as crypto from 'crypto';
import * as jwt from 'jsonwebtoken';
import * as forge from 'node-forge';
import CryptoJS from 'crypto-js';

export class PaymentService {
  private readonly logger = new Logger(PaymentService.name);

  constructor(
    private readonly gatewayProxyService: GatewayProxyService,
  ) {}

  validateCardInfo(paymentInfo) {
     if (!paymentInfo || !paymentInfo.cardNumber || !paymentInfo.expirationDate || !paymentInfo.cvv) {
         throw new Error('Invalid card information');
     }

     const cardNumberRegex = /^\d{16}$/;
     if (!cardNumberRegex.test(paymentInfo.cardNumber)) {
         throw new Error('Invalid card number. It should be a 16-digit number.');
     }

     const expirationDateRegex = /^(0[1-9]|1[0-2])\/\d{2}$/;
     if (!expirationDateRegex.test(paymentInfo.expirationDate)) {
         throw new Error('Invalid expiration date. It should be in the format MM/YY.');
     }

     const cvvRegex = /^\d{3}$/;
     if (!cvvRegex.test(paymentInfo.cvv)) {
         throw new Error('Invalid CVV. It should be a 3-digit number.');
     }
 }
  async retrieveLocation(): Promise<string> {
    try {
      const ipInfoResponse = await axios.get('https://ipinfo.io');
      const location = ipInfoResponse.data.loc;
      if (!location) {
        throw new Error('No location information available');
      }
      //console.debug('Location:', location);
      return location;
    } catch (error) {
      throw new Error('Error retrieving location information: ' + error.message);
    }
  }
  // a modifier
async processCardInfo(paymentInfo: PaymentInfoDTO, applicationId: string,token: string ): Promise<Buffer> {
 try {
    this.validateCardInfo(paymentInfo);
    const location = await this.retrieveLocation();
    const [altitude, longitude] = location.split(',');

      const publicKey = await this.gatewayProxyService.getPublicKey(applicationId);

const cardInfo = {
    cardNumber: paymentInfo.cardNumber,
    expirationDate: paymentInfo.expirationDate,
    cvv: paymentInfo.cvv,
};

      const encryptedCardInfo = crypto.publicEncrypt(publicKey, Buffer.from(JSON.stringify(cardInfo)));
    const payment = {
      cryptedCreditCard: encryptedCardInfo,
      amount: paymentInfo.amount,
      token: token,};
    this.logger.debug('Payment:', payment);
    await this.gatewayProxyService.processPayment(JSON.stringify(payment));
    return encryptedCardInfo;
  } catch (error) {
    throw new Error('Error processing card information: ' + error.message);
  }
}

}
