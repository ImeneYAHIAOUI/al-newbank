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
    // a modifier
  validateCardInfo(paymentInfo: PaymentInfoDTO): void {
    if (!paymentInfo || !paymentInfo.cardNumber || !paymentInfo.expirationDate || !paymentInfo.cvv) {
      throw new Error('Invalid card information');
    }
  }
  // a modifier
  async retrieveLocation(): Promise<string> {
    try {
      const ipInfoResponse = await axios.get('https://ipinfo.io');
      const location = ipInfoResponse.data.loc;
      if (!location) {
        throw new Error('No location information available');
      }
      console.debug('Location:', location);
      return location;
    } catch (error) {
      throw new Error('Error retrieving location information: ' + error.message);
    }
  }
  // a modifier
async processCardInfo(paymentInfo: PaymentInfoDTO, applicationId: string,token: string ): Promise<String> {
 // try {
    this.validateCardInfo(paymentInfo);
    const location = await this.retrieveLocation();
    const [altitude, longitude] = location.split(',');
const card = {
  cardNumber: paymentInfo.cardNumber,
  expirationDate: paymentInfo.expirationDate,
  cvv: paymentInfo.cvv,
};
    const secretKey = process.env.ACCESS_TOKEN_SECRET;
    const encryptedCardInfo = jwt.sign(
            {
                  cardNumber: paymentInfo.cardNumber,
                  expirationDate: paymentInfo.expirationDate,
                  cvv: paymentInfo.cvv,
            },
            secretKey,
            {
                expiresIn: '1h',
                algorithm: 'HS256'
            }
        );
    const payment = {
      cryptedCreditCard: encryptedCardInfo,
      amount: paymentInfo.amount,
      token: token,};
    this.logger.debug('Payment:', payment);
    await this.gatewayProxyService.processPayment(JSON.stringify(payment));
    return encryptedCardInfo;
  //} catch (error) {
    //throw new Error('Error processing card information: ' + error.message);
  //}
}

// a modifier
  verifyAccessToken(token: string): boolean {
    try {
      const decoded = jwt.verify(token, process.env.ACCESS_TOKEN_SECRET);
      this.logger.log('Access token verified successfully');
      return true;
    } catch (error) {
      this.logger.error(`Error verifying access token: ${error.message}`);
      return false;
    }
  }
// a modifier
  generateAccessToken(id: string, secretKey: string): string {
    const accessToken = jwt.sign({ id }, secretKey, { expiresIn: '1y' });
    return accessToken;
  }

}
