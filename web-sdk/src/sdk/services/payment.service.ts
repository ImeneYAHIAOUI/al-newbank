import { Injectable, Logger, HttpException, HttpStatus } from '@nestjs/common';
import { TokenMockService } from '../services/token-mock.service';
import { GatewayProxyService } from '../services/gateaway-proxy/gateaway-proxy.service';
import axios from 'axios';
import * as crypto from 'crypto';

@Injectable()
export class PaymentService {
  constructor(
    private readonly tokenMockService: TokenMockService,
    private readonly gatewayProxyService: GatewayProxyService
  ) {}

  validateToken(token: string): void {
    if (!this.tokenMockService.verifyAccessToken(token)) {
      throw new Error('Invalid access token');
    }
  }

  validateCardInfo(cardInfo: any): void {
    if (!cardInfo || !cardInfo.cardNumber || !cardInfo.expirationDate || !cardInfo.cvv) {
      throw new Error('Invalid card information');
    }
  }

  async retrieveLocation(): Promise<string> {
    try {
      const ipInfoResponse = await axios.get('https://ipinfo.io');
      const location = ipInfoResponse.data.loc;
      if (!location) {
        throw new Error('Unable to retrieve location information');
      }
      console.debug('Location:', location);
      return location;
    } catch (error) {
      throw new Error('Error retrieving location information');
    }
  }

  async processCardInfo(PaymentInfo: any, token: string): Promise<Buffer> {
    this.validateToken(token);
    this.validateCardInfo(PaymentInfo);

    const location = await this.retrieveLocation();
    const [altitude, longitude] = location.split(',');

    const payload = {
      cardNumber: PaymentInfo.cardNumber,
      expirationDate: PaymentInfo.expirationDate,
      cvv: PaymentInfo.cvv,
      altitude,
      longitude,
    };

    const publicKey = await this.gatewayProxyService.getPublicKey();
    const encryptedCardInfo = crypto.publicEncrypt(publicKey, Buffer.from(JSON.stringify(payload)));

    console.debug('Encrypted Card Information:', encryptedCardInfo.toString('base64'));
        this.gatewayProxyService.processPayment(PaymentInfo.amount, encryptedCardInfo.toString('base64'));


    return encryptedCardInfo;
  }
}

