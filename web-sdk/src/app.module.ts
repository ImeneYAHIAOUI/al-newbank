import { Module } from '@nestjs/common';
import { CacheModule } from '@nestjs/cache-manager';
import { HttpModule } from '@nestjs/axios';
import { AppService } from './sdk/services/app.service';
import appConfig from './shared/config/app.config';
import swaggeruiConfig from './shared/config/swaggerui.config';
import { TokenMockService } from './sdk/services/token-mock.service';
import { GatewayProxyService } from './sdk/services/gateaway-proxy/gateaway-proxy.service';
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
  providers: [AppService,PaymentService,TokenMockService,GatewayProxyService],
})
export class AppModule {}
