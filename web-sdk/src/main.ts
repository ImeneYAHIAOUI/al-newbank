import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import * as express from 'express';
import { join } from 'path';
import { GatewayProxyService } from './sdk/services/gateway-proxy/gateway-proxy.service';
import {IntegrationService} from './sdk/services/integration.service';
import {PaymentService} from './sdk/services/payment.service';
import { AppModule } from './app.module';
import { prompt } from 'inquirer';
import {PaymentInfoDTO} from './sdk/dto/payment-info.dto';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableCors();

  const configService = app.get(ConfigService);

  app.useGlobalPipes(new ValidationPipe());

  app.use(express.static(join(__dirname, '..', 'public')));

  app.enableShutdownHooks();
  const gatewayProxyService = app.get(GatewayProxyService);

 const integrationService = new IntegrationService(gatewayProxyService);
  const applicationInfo = await prompt([
    {
      type: 'input',
      name: 'IBAN',
      message: 'Entrez l\'IBAN de l\'application:'
    },
    {
      type: 'input',
      name: 'BIC',
      message: 'Entrez le BIC de l\'application:'
    },
    {
      type: 'input',
      name: 'name',
      message: 'Entrez le nom de l\'application:'
    },
    {
      type: 'input',
      name: 'email',
      message: 'Entrez l\'adresse e-mail de l\'application:'
    },
    {
      type: 'input',
      name: 'url',
      message: 'Entrez l\'URL de l\'application:'
    },
    {
      type: 'input',
      name: 'description',
      message: 'Entrez la description de l\'application:'
    }
  ]);
  const result = await integrationService.integrateService(applicationInfo);
    const paymentInfo : PaymentInfoDTO = await prompt([
      {
        type: 'input',
        name: 'cardNumber',
        message: 'Entrez cardNumber:'
      },
      {
        type: 'input',
        name: 'cvv',
        message: 'Entrez cvv:'
      },
      {
        type: 'input',
        name: 'expirationDate',
        message: 'Entrez expirationDate:'
      },
      {
        type: 'input',
        name: 'amount',
        message: 'Entrez amount:'
      }
    ]);
    const paymentService=new PaymentService(gatewayProxyService);
    paymentService.processCardInfo(paymentInfo,result.id,result.apiKey);

}

bootstrap();
