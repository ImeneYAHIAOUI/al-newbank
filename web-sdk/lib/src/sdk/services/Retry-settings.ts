export class RetrySettings {
  retries: number;
  factor: number;
  minTimeout: number;
  maxTimeout: number;
  randomize: boolean;

  constructor(options: {
    retries?: number;
    factor?: number;
    minTimeout?: number;
    maxTimeout?: number;
    randomize?: boolean;
  } = {}) {
    this.retries = options.retries || 3;
    this.factor = options.factor || 2;
    this.minTimeout = options.minTimeout || 1000;
    this.maxTimeout = options.maxTimeout || 3000;
    this.randomize = options.randomize || true;
  }
}
