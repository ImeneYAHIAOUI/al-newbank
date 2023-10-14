import { Controller, Post, Body } from '@nestjs/common';
import * as jwt from 'jsonwebtoken';

  @Controller('payment')
  export class PaymentController {
    @Post()
    processPayment(@Body() cardInfo: any) {

      try {
        if (!cardInfo || !cardInfo.cardNumber || !cardInfo.expirationDate || !cardInfo.cvv) {
           throw new Error('Invalid card information');
        }
        const payload = {
          cardNumber: cardInfo.cardNumber,
          expirationDate: cardInfo.expirationDate,
          cvv: cardInfo.cvv,
        };
        const token = jwt.sign(payload, process.env.ACCESS_TOKEN_SECRET, { expiresIn: '1800s' }); // 1800s = 30min
        //envoyer au gateway
        console.debug('Received Card Information:', cardInfo);
        console.debug('Generated Token:', token);
        return { message: 'Payment received successfully!' };
      } catch (error) {
        console.error('Error processing payment:', error);
        return { message: 'Error processing payment' };
      }
    }

}
