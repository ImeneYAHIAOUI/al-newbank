import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import * as express from 'express';
import { join } from 'path';
import { GatewayProxyService } from './sdk/services/gateway-proxy/gateway-proxy.service';
import {IntegrationService} from './sdk/services/integration.service';
import { AppModule } from './app.module';
import {ApplicationInfo} from './sdk/dto/application-info.dto';
async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableCors();

  const configService = app.get(ConfigService);

  app.useGlobalPipes(new ValidationPipe());

  app.use(express.static(join(__dirname, '..', 'public')));

  app.enableShutdownHooks();
  const gatewayProxyService = app.get(GatewayProxyService);

  const integrationService = new IntegrationService(gatewayProxyService);
  const applicationInfo: ApplicationInfo = {
    IBAN: 'vossssdsdstrqessssssss4i_iban',
    BIC: 'votsre5sqsssssssdi_bic',
     name: 'Mossjn Apsqssssplicsssssaltion',
        email: 'conssstssacqtm@mo*nasspplsssisscation.com',
        url: 'https://monsssasssssslpplicaddtion.com',
        description: 'Desssscriptioùn de mon application'
  };


  integrationService.integrateService(applicationInfo)
    .then(result => {
      console.log('Intégration réussie !');
      console.log('ID de l\'application:', result.id);
      console.log('API Key:', result.apiKey);
    })
    .catch(error => {
      console.error('Erreur lors de l\'intégration:', error.message);
    });

}

bootstrap();
