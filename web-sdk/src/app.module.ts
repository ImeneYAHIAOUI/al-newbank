import { Module } from '@nestjs/common';
import { CacheModule } from '@nestjs/cache-manager';
import { HttpModule } from '@nestjs/axios';
import { AppService } from './sdk/services/app.service';
import appConfig from './shared/config/app.config';
import swaggeruiConfig from './shared/config/swaggerui.config';
import { GatewayProxyService } from './sdk/services/gateway-proxy/gateway-proxy.service';
import dependenciesConfig from './shared/config/dependencies.config';
import { PaymentService } from './sdk/services/payment.service';
import { ConfigModule } from '@nestjs/config';
@Module({
 imports: [
     ConfigModule.forRoot({
       isGlobal: true,
       load: [appConfig, swaggeruiConfig,dependenciesConfig],
     }), CacheModule.register({
                                ttl: 3600,
                                max : 100
                              }),HttpModule],
  controllers: [],
  providers: [GatewayProxyService],
})
export class AppModule {}
