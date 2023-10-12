import { Module } from '@nestjs/common';
import { CacheModule } from '@nestjs/cache-manager';
import {AppController } from './sdk/controllers/app.controller';
import { AppService } from './sdk/services/app.service';
import appConfig from './shared/config/app.config';
import swaggeruiConfig from './shared/config/swaggerui.config';

import { ConfigModule } from '@nestjs/config';
@Module({
 imports: [
     ConfigModule.forRoot({
       isGlobal: true,
       load: [appConfig, swaggeruiConfig],
     }), CacheModule.register({
                                ttl: 3600,
                                max : 100
                              }),],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
