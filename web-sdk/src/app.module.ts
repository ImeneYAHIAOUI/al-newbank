import { Module } from '@nestjs/common';
import { CacheModule } from '@nestjs/cache-manager';
import { HttpModule } from '@nestjs/axios';
import appConfig from './shared/config/app.config';
import { GatewayProxyService } from './sdk/services/gateway-proxy/gateway-proxy.service';
import dependenciesConfig from './shared/config/dependencies.config';
import { ConfigModule } from '@nestjs/config';
import { IntegrationService } from './sdk/services/integration.service';
@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      load: [appConfig, dependenciesConfig],
    }),
    CacheModule.register({
      ttl: 3600,
      max: 100,
    }),
    HttpModule,
  ],
  controllers: [],
  providers: [GatewayProxyService, IntegrationService],
})
export class AppModule {}
