import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import * as express from 'express'; // Importer express
import { join } from 'path';

import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableCors();

  // Retrieve config service
  const configService = app.get(ConfigService);

  // Add validation pipe for all endpoints
  app.useGlobalPipes(new ValidationPipe());

  // Servir les fichiers statiques
  app.use(express.static(join(__dirname, '..', 'public')));

  // Starts listening for shutdown hooks
  app.enableShutdownHooks();

  // Run the app
  const appPort = configService.get('app.port');
  console.log(appPort);
  await app.listen(appPort);
}

bootstrap();
