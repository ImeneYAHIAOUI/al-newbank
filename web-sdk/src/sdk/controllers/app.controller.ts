import { Controller, Get, Headers, Body, Post, BadRequestException, Logger } from '@nestjs/common';
import { AppService } from '../services/app.service';
import { TokenMockService } from '../services/token-mock.service';

@Controller()
export class AppController {
  private readonly logger = new Logger(AppController.name);

  constructor(
    private readonly appService: AppService,
    private readonly mockService: TokenMockService,
  ) {}

  @Get()
  getService(@Headers('authorization') token: string): string {
    if (!this.mockService.verifyAccessToken(token)) {
      throw new InvalidTokenException('Invalid access token');
    }
    return this.appService.getService();
  }

  @Post('generate-token')
  generateToken(@Body() body: { secretKey: string }): string {
    const secretKey: string = body.secretKey;

    if (!secretKey) {
      throw new BadRequestException('A secret key is required.');
    }

    if (secretKey !== process.env.ACCESS_TOKEN_SECRET) {
      throw new BadRequestException('The secret key is not valid.');
    }
    const accessToken: string = this.mockService.generateAccessToken(process.env.ACCESS_TOKEN_SECRET);

    this.logger.log(`Generated Access Token for secret key: ${secretKey} ${accessToken}`);

    return accessToken;
  }
}
