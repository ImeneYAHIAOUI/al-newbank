import { HttpException, HttpStatus } from '@nestjs/common';

export class ServiceUnavailableException extends Error {
  constructor(message: string = 'Service Unavailable', retryAfter?: number) {
    super(`${message}. Retry after ${retryAfter}`);
    this.name = 'ServiceUnavailable';
  }
}
