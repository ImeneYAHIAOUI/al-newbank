import axios from 'axios';
import { MetricsServer } from './Metrics-server';

export class MetricsReporter {
  private readonly metricsServer;
  private readonly baseURL: string;

  constructor(baseURL: string, metricsPort: number) {
    this.baseURL = baseURL;
    this.metricsServer = new MetricsServer(metricsPort);
    this.metricsServer.startServer();
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

