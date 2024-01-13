import {GatewayAuthorizationProxyService} from './gateway-authorization-proxy/gateway-authorization-proxy.service';
import {PaymentInfoDTO} from '../dto/payment-info.dto';
import * as crypto from 'crypto';
import {PaymentDto} from "../dto/payment.dto";
import {AuthorizeDto} from "../dto/authorise.dto";
import {GatewayConfirmationProxyService} from './gateway-confirmation-proxy/gateway-confirmation-proxy.service';
import {RetrySettings} from "./Retry-settings";

import { StatusReporterProxyService } from './status-reporter-proxy/status-reporter-proxy.service';

export class PaymentService {
  private readonly gatewayAuthorizationProxyService;
  private readonly gatewayConfirmationProxyService;
  private readonly statusReporterProxyService;
  private readonly config = require('./config');
constructor(retrySettings: RetrySettings) {

  this.statusReporterProxyService = new StatusReporterProxyService(retrySettings);
  this.gatewayAuthorizationProxyService = new GatewayAuthorizationProxyService(this.config.load_balancer_host,retrySettings, this.statusReporterProxyService);
  this.gatewayConfirmationProxyService = new GatewayConfirmationProxyService(this.config.load_balancer_host,retrySettings, this.statusReporterProxyService);
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


   async getPublicKey(token: string): Promise<string> {
     return await this.gatewayAuthorizationProxyService.getPublicKey(token);
   }



  async processPayment(encryptedCardInfo: string, token: string,
     amount: string
  )   {
      const payment : PaymentDto = {
        encryptedCard: encryptedCardInfo,
        amount: amount,
      };
      console.debug('payment request sent');
      const auth : AuthorizeDto = await this.gatewayAuthorizationProxyService.authorizePaymentWithRetry(payment, token);
      console.debug('payment authorized');
      return auth;
  }

  private encrypteCreditCard(paymentInfo: PaymentInfoDTO, publicKey: string) {
    this.validateCardInfo(paymentInfo);

    const cardInfo = {
      cardNumber: paymentInfo.cardNumber,
      expirationDate: paymentInfo.expirationDate,
      cvv: paymentInfo.cvv,
    };

    const plaintext = `${paymentInfo.cardNumber.toString()},${paymentInfo.expirationDate.toString()},${paymentInfo.cvv.toString()}`;

    const buffer = Buffer.from(plaintext, 'utf8');
    const encryptedCardInfo = crypto.publicEncrypt(
        '-----BEGIN PUBLIC KEY-----\n' +
        publicKey +
        '\n-----END PUBLIC KEY-----',
        buffer,
    );
    return encryptedCardInfo.toString('base64');
  }

  async authorize(paymentInfo: PaymentInfoDTO,token: string) {

       try {

          const publicKey = await this.getPublicKey(token);
          const encryptedCardInfo = this.encrypteCreditCard(paymentInfo, publicKey);
          const result=await this.processPayment(encryptedCardInfo, token, paymentInfo.amount);
          return result;
          } catch (error) {
                throw error;
        }
      
  
  }
    
  async confirmPayment(transactionId: string, token: string){
    console.debug('payment confirmation request sent');

    try{

      return await this.gatewayConfirmationProxyService.confirmPayment(transactionId, token);
    } catch (error) {
      throw error;
    }
  }

  async pay(paymentInfo: PaymentInfoDTO, token: string) {
    const auth = await this.authorize(paymentInfo, token);
    return await this.confirmPayment(auth.transactionId, token);
  }

}