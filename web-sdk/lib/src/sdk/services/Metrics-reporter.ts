import axios from 'axios';

export class MetricsReporter {
  private readonly baseURL: string;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
  }

  async sendPostRequest(endpoint: string) {
    try {
      const response = await axios.post(`${this.baseURL}${endpoint}`);
      console.debug(response.data);
    } catch (error) {
      console.error(`Error encountered while sending metrics to the server - ${this.baseURL}${endpoint} -: ${error instanceof Error ? error.message : error}`);
    }
  }
}

