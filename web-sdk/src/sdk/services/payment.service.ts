import { Injectable, Logger, HttpException, HttpStatus } from '@nestjs/common';
import { TokenMockService } from '../services/token-mock.service';
import { GatewayProxyService } from '../services/gateaway-proxy/gateaway-proxy.service';
import { PaymentInfoDTO } from '../dto/payment-info.dto';
import {InvalidTokenException} from '../exceptions/invalid-token.exception';
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
      throw new InvalidTokenException('Invalid access token');
    }
  }

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
        throw new Error('Unable to retrieve location information');
      }
      console.debug('Location:', location);
      return location;
    } catch (error) {
      throw new Error('Error retrieving location information');
    }
  }

  async processCardInfo(paymentInfo: PaymentInfoDTO, token: string): Promise<Buffer> {
    this.validateToken(token);
    this.validateCardInfo(paymentInfo);
    const location = await this.retrieveLocation();
    const [altitude, longitude] = location.split(',');
    const publicKey = await this.gatewayProxyService.getPublicKey();
      const payload = {
          cardNumber: paymentInfo.cardNumber,
          expirationDate: paymentInfo.expirationDate,
          cvv: paymentInfo.cvv,
          altitude : altitude,
          longitude :longitude,
          amount : paymentInfo.amount,
          token : paymentInfo.token,
        };
    const encryptedCardInfo = crypto.publicEncrypt(publicKey, Buffer.from(JSON.stringify(payload)));
    console.debug('Encrypted Card Information:', encryptedCardInfo.toString('base64'));
        this.gatewayProxyService.processPayment(encryptedCardInfo.toString('base64'));
    return encryptedCardInfo;
  }
}

