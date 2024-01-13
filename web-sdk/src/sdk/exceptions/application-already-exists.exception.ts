export class ApplicationAlreadyExists extends Error {
  constructor(message = 'Application already exists') {
    super(message);
    this.name = 'Application already exists';
  }
}
