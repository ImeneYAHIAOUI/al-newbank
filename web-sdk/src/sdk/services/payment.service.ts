import { Injectable } from '@nestjs/common';
import { TokenMockService } from '../services/token-mock.service';
import { GatewayProxyService } from '../services/gateaway-proxy/gateaway-proxy.service';
import axios from 'axios';
import * as crypto from 'crypto';

@Injectable()
export class PaymentService {
  constructor(private readonly tokenMockService: TokenMockService,private readonly gatewayProxyService: GatewayProxyService){}

  validateToken(token: string) {
    if (!this.tokenMockService.verifyAccessToken(token)) {
      throw new Error('Invalid access token');
    }
  }

  validateCardInfo(cardInfo: any) {
    if (!cardInfo || !cardInfo.cardNumber || !cardInfo.expirationDate || !cardInfo.cvv) {
      throw new Error('Invalid card information');
    }
  }

  async retrieveLocation() {
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

  async processCardInfo(merchantId: string, cardInfo: any, token: string) {
    this.validateToken(token);

    this.validateCardInfo(cardInfo);

    const location = await this.retrieveLocation();
    const [altitude, longitude] = location.split(',');

    const payload = {
      cardNumber: cardInfo.cardNumber,
      expirationDate: cardInfo.expirationDate,
      cvv: cardInfo.cvv,
      altitude,
      longitude,
    };

    const publicKey = await this.gatewayProxyService.getPublicKey(merchantId);

    const encryptedCardInfo = crypto.publicEncrypt(publicKey, Buffer.from(JSON.stringify(payload)));

    console.debug('Encrypted Card Information:', encryptedCardInfo.toString('base64'));

    return encryptedCardInfo;
  }

}
