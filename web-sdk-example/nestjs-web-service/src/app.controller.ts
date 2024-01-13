import {Body, Controller, Get, Post} from '@nestjs/common';
import { AppService } from './app.service';
import {PaymentInfoDTO} from "@teamb/newbank-sdk";

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get()
    getHello(): string {
        return this.appService.getHello();
    }

    @Post('/authorize')
    authorizePayment(@Body() paymentInfoDTO: PaymentInfoDTO): void {
        this.appService.authorizePayment(paymentInfoDTO);
    }

    @Post('/confirm')
    confirmPayment(@Body() transactionId: string): void {
        this.appService.confirmPayment(transactionId);
    }

    @Post('/pay')
    async payment(@Body() paymentInfoDTO: PaymentInfoDTO): Promise<void> {
       await this.appService.payment(paymentInfoDTO);
    }

    @Get('/backend-status')
    getBackendStatus() {
      return this.appService.getBackendStatus();
    }

    @Post('/metrics')
    getMetrics(@Body() body: any) {
      return this.appService.getMetrics(body);
    }
}
