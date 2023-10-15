import { Controller, Post, Body, Headers } from '@nestjs/common';
import * as crypto from 'crypto';
import axios from 'axios';
import { PaymentService } from '../services/payment.service';

@Controller('payment')
export class PaymentController {
  constructor(private readonly paymentService: PaymentService) {}

  @Post()
  async processPayment(@Body() cardInfo: any, @Headers('Authorization') token: string) {
    try {
      const parsedCardInfo = this.paymentService.processCardInfo(cardInfo, token);
      return { message: 'Payment received successfully!' };
    } catch (error) {
      console.error('Error processing payment:', error);
      return { message: 'Error processing payment' };
    }
  }
}
