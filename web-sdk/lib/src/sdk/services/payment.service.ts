import { GatewayProxyService } from './gateway-proxy/gateway-proxy.service';
import { PaymentInfoDTO } from '../dto/payment-info.dto';
import axios from 'axios';
import * as crypto from 'crypto';

export class PaymentService {
  private readonly gatewayProxyService;

  constructor(loadBalancerHost: string) {
    this.gatewayProxyService = new GatewayProxyService(loadBalancerHost);
  }

  validateCardInfo(paymentInfo: PaymentInfoDTO): void {
    if (
      !paymentInfo ||
      !paymentInfo.cardNumber ||
      !paymentInfo.expirationDate ||
      !paymentInfo.cvv
    ) {
      throw new Error('Invalid card information');
    }

    const cardNumberRegex = /^\d{16}$/;
    if (!cardNumberRegex.test(paymentInfo.cardNumber)) {
      throw new Error('Invalid card number. It should be a 16-digit number.');
    }

    const expirationDateRegex = /^(0[1-9]|1[0-2])\/\d{4}$/;
    if (!expirationDateRegex.test(paymentInfo.expirationDate)) {
      throw new Error(
        'Invalid expiration date. It should be in the format MM/YYYY.',
      );
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
      console.debug('Location:', location);
      return location;
    } catch (error: any) {
      throw new Error(
        'Error retrieving location information: ' + error.message,
      );
    }
  }

  async processCardInfo(
    paymentInfo: PaymentInfoDTO,
    applicationId: string,
    token: string,
  ): Promise<Buffer> {
    try {
      this.validateCardInfo(paymentInfo);

      const publicKey =
        await this.gatewayProxyService.getPublicKey(applicationId);
      console.debug('Public key:', publicKey);

      const cardInfo = {
        cardNumber: paymentInfo.cardNumber,
        expirationDate: paymentInfo.expirationDate,
        cvv: paymentInfo.cvv,
      };
      console.debug('Card info:', Buffer.from(JSON.stringify(cardInfo)));

      const plaintext = `${paymentInfo.cardNumber.toString()},${paymentInfo.expirationDate.toString()},${paymentInfo.cvv.toString()}`;
      console.debug('Plaintext:', plaintext);

      const buffer = Buffer.from(plaintext, 'utf8');
      const encryptedCardInfo = crypto.publicEncrypt(
        '-----BEGIN PUBLIC KEY-----\n' +
          publicKey +
          '\n-----END PUBLIC KEY-----',
        buffer,
      );

      const payment = {
        cryptedCreditCard: encryptedCardInfo.toString('base64'),
        amount: paymentInfo.amount,
        token: token,
      };
      console.debug('Payment:', payment);

      await this.gatewayProxyService.processPayment(JSON.stringify(payment));
      console.debug('payment request sent');
      return encryptedCardInfo;
    } catch (error: any) {
      throw new Error('Error processing card information: ' + error.message);
    }
  }
}
