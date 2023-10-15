import { Controller, Get, Headers, Body, Post, BadRequestException, Logger } from '@nestjs/common';
import { AppService } from '../services/app.service';
import { TokenMockService } from '../services/token-mock.service';
import { ApplicationInfo } from '../dto/application-info.dto';
import { InvalidTokenException } from '../exceptions/invalid-token.exception';

@Controller()
export class AppController {
  private readonly logger = new Logger(AppController.name);

  constructor(
    private readonly appService: AppService,
    private readonly mockService: TokenMockService,
  ) {}

  @Post()
  async getService(@Body() application: ApplicationInfo, @Headers('authorization') token: string): Promise<string> {
    try {
      if (!this.mockService.verifyAccessToken(token)) {
        throw new InvalidTokenException('Invalid access token');
      }

      const result = await this.appService.getService(application);

      return result;
    } catch (error) {
      throw new BadRequestException(error.message);
    }
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

