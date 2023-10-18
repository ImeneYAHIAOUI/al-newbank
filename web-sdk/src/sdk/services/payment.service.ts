import { Logger, HttpException, HttpStatus } from '@nestjs/common';
import { GatewayProxyService } from '../services/gateway-proxy/gateway-proxy.service';
import { PaymentInfoDTO } from '../dto/payment-info.dto';
import { InvalidTokenException } from '../exceptions/invalid-token.exception';
import axios from 'axios';
import * as crypto from 'crypto';
import * as jwt from 'jsonwebtoken';

export class PaymentService {
  private readonly logger = new Logger(PaymentService.name);
  private _applicationId: string;


  constructor(
    private readonly gatewayProxyService: GatewayProxyService,
  ) {}

  validateCardInfo(paymentInfo: PaymentInfoDTO): void {
    if (!paymentInfo || !paymentInfo.cardNumber || !paymentInfo.expirationDate || !paymentInfo.cvv) {
      throw new Error('Invalid card information');
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
    } catch (error) {
      throw new Error('Error retrieving location information: ' + error.message);
    }
  }

  async processCardInfo(paymentInfo: PaymentInfoDTO): Promise<Buffer> {
    try {
      this.validateCardInfo(paymentInfo);
      const location = await this.retrieveLocation();
      const [altitude, longitude] = location.split(',');
      const token = this.generateAccessToken(this._applicationId, process.env.ACCESS_TOKEN_SECRET);
      const publicKey = await this.gatewayProxyService.getPublicKey(this._applicationId);
      const payload = {
        cardNumber: paymentInfo.cardNumber,
        expirationDate: paymentInfo.expirationDate,
        cvv: paymentInfo.cvv,
        altitude: altitude,
        longitude: longitude,
        amount: paymentInfo.amount,
        token: token,
      };
      const encryptedCardInfo = crypto.publicEncrypt(publicKey, Buffer.from(JSON.stringify(payload)));
      console.debug('Encrypted Card Information:', encryptedCardInfo.toString('base64'));
      this.gatewayProxyService.processPayment(encryptedCardInfo.toString('base64'));
      return encryptedCardInfo;
    } catch (error) {
      throw new Error('Error processing card information: ' + error.message);
    }
  }

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

  generateAccessToken(id: string, secretKey: string): string {
    const accessToken = jwt.sign({ id }, secretKey, { expiresIn: '1y' });
    return accessToken;
  }

  set applicationId(value: string) {
    this._applicationId = value;
  }
}
