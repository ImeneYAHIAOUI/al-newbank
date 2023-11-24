const axios = require('axios');
import {MetricsServer} from "./Metrics-server";

class MetricsReporter {
    private readonly metricsServer ;

  constructor(baseURL, metricsPort: number) {
    this.baseURL = baseURL;
    this.metricsServer = new MetricsServer(metricsPort);
    this.metricsServer.startServer();
  }

  async sendPostRequest(endpoint) {
    try {
      const response = await axios.post(`${this.baseURL}${endpoint}`);
      console.log(response.data);
    } catch (error) {
      console.error(`Error sending POST request to ${endpoint}:`, error.message);
    }
  }
}


