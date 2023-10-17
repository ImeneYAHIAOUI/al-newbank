import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import * as express from 'express';
import { join } from 'path';

import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableCors();

  const configService = app.get(ConfigService);

  app.useGlobalPipes(new ValidationPipe());

  app.use(express.static(join(__dirname, '..', 'public')));

  app.enableShutdownHooks();

}

bootstrap();
