import { Logger, HttpException, HttpStatus } from '@nestjs/common';
import { GatewayProxyService } from '../services/gateway-proxy/gateway-proxy.service';
import { PaymentInfoDTO } from '../dto/payment-info.dto';
import { InvalidTokenException } from '../exceptions/invalid-token.exception';
import axios from 'axios';
import * as crypto from 'crypto';
import * as jwt from 'jsonwebtoken';
import * as forge from 'node-forge';

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
  async processCardInfo(paymentInfo: PaymentInfoDTO,applicationId : string): Promise<Buffer> {
    try {
      this.validateCardInfo(paymentInfo);
      const location = await this.retrieveLocation();
      const [altitude, longitude] = location.split(',');
      const token = this.generateAccessToken(applicationId, process.env.ACCESS_TOKEN_SECRET);
      const r=await this.gatewayProxyService.getPublicKey(applicationId);
      const payload = {
        cardNumber: paymentInfo.cardNumber,
        expirationDate: paymentInfo.expirationDate,
        cvv: paymentInfo.cvv,
        altitude: altitude,
        longitude: longitude,
        amount: paymentInfo.amount,
        token: token,
      };
      const publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkgyTQdGmxSBb7JIY6xE7bO3r7PmfKTLbKN8c0AGUy2Avu07pO6wy27pFhOD2PETX2qVr7LqGw8c5KbEXqhenGUmyAYbvmusctY+4zZV84Vyhef0qW6+JWO20hPlRgXaO/IQ6LZOhbWSZkDnu6VUFjh7dIuxq2Gu9xhHZZ6lsqXpjBc41sn5+SF7DmaHPSLguFpySgiiSbetlosv1WkDBghJ0McSwKjc0QtCpQQhgypXrmFnh0+FVvEGVEvEJDmQfoVNgmpqcalV9/DZgRsozpBvHiSUUUEa5ui3UkhusKPknluXfeXiG7juhZW1lagaHur+8LQBBjsfzm6ooBs/zkwIDAQAB";
         const publicKeyBuffer = Buffer.from(publicKeyString, 'base64');
         const key = crypto.createPublicKey(publicKeyBuffer);
      const encryptedCardInfo = crypto.publicEncrypt(key, Buffer.from(JSON.stringify(payload)));
      console.debug('Encrypted Card Information:', encryptedCardInfo.toString('base64'));
      this.gatewayProxyService.processPayment(encryptedCardInfo.toString('base64'));
      return encryptedCardInfo;
    } catch (error) {
      throw new Error('Error processing card information: ' + error.message);
    }
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
