import { Controller,Param, Post, Body, Headers } from '@nestjs/common';
import * as crypto from 'crypto';
import axios from 'axios';
import { PaymentService } from '../services/payment.service';

@Controller('payment')
export class PaymentController {
  constructor(private readonly paymentService: PaymentService) {}

@Post()
async processPayment(@Body() PaymentInfo: any, @Headers('Authorization') token: string) {
  try {
    const parsedCardInfo = await this.paymentService.processCardInfo( PaymentInfo, token);
    return { message: 'Payment received successfully!' };
  } catch (error) {
    console.error('Error processing payment:', error);
    return { message: 'Error processing payment' };
  }
}

}
