export class ServerInternalError extends Error {
  constructor(message = 'Internal Server Error: The server encountered an unexpected issue') {
    super(message);
    this.name = 'InternalServerIssue';
  }
}