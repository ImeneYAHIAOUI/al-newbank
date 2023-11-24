"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const axios = require('axios');
const Metrics_server_1 = require("./Metrics-server");
class MetricsReporter {
    constructor(baseURL, metricsPort) {
        this.baseURL = baseURL;
        this.metricsServer = new Metrics_server_1.MetricsServer(metricsPort);
        this.metricsServer.startServer();
    }
    async sendPostRequest(endpoint) {
        try {
            const response = await axios.post(`${this.baseURL}${endpoint}`);
            console.log(response.data);
        }
        catch (error) {
            console.error(`Error sending POST request to ${endpoint}:`, error.message);
        }
    }
}
//# sourceMappingURL=metrics-reporter.js.map