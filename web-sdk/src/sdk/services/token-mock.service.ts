import { Injectable, Logger } from '@nestjs/common';
import * as jwt from 'jsonwebtoken';
import { ApplicationInfo } from '../dto/application-info.dto';
import { InvalidTokenException } from '../exceptions/invalid-token.exception';

@Injectable()
export class TokenMockService {
  private readonly logger = new Logger(TokenMockService.name);

  verifyAccessToken(token: string): boolean {
    try {
      const decoded = jwt.verify(token, process.env.ACCESS_TOKEN_SECRET);
      this.logger.log('Access token verified successfully');
      return true;
    } catch (error) {
      this.logger.error(`Error verifying access token: ${error.message}`);
      return false;
    }
  }
    generateAccessToken(secretKey: string): string {
      const accessToken = jwt.sign({}, secretKey, { expiresIn: '1y' });
      return accessToken;
    }
}
