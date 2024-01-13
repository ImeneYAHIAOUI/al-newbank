export class InvalidTokenException extends Error {
  constructor(message = 'Token invalide') {
    super(message);
    this.name = 'InvalidTokenException';
  }
}
