// payment.controller.ts
import { Controller, Post, Body } from '@nestjs/common';

@Controller('payment')
export class PaymentController {
  @Post()
  processPayment(@Body() cardInfo: any) {
    console.log('Données de la carte reçues :', cardInfo);
    return { message: 'Paiement reçu avec succès !' };
  }
}
