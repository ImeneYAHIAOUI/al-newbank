import axios from 'axios';

export class MetricsReporter {
  private readonly baseURL: string;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
  }

  async sendPostRequest(endpoint: string) {
    try {
      const response = await axios.post(`${this.baseURL}${endpoint}`);
      console.log(response.data);
    } catch (error) {
      console.error(`Error sending POST request to ${endpoint}:`, (error as Error).message || error);
    }
  }
}

