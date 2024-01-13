export class ApplicationNotFound extends Error {
  constructor(message = 'Application not found') {
    super(message);
    this.name = 'Application not found';
  }
}
