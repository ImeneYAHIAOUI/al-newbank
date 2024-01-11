import { HttpException, HttpStatus } from '@nestjs/common';

export class ServiceUnavailableException extends HttpException {
  constructor(message: string = 'Service Unavailable', retryAfter?: number) {
    const headers: Record<string, string> = {};
    
    if (retryAfter) {
      headers['Retry-After'] = retryAfter.toString();
    }

    super({ message, headers }, HttpStatus.SERVICE_UNAVAILABLE);
  }
}