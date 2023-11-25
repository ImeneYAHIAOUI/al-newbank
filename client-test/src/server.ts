// server.ts
import { MetricsServer } from "@teamb/newbank-sdk/dist/sdk/services/Metrics-server";
 async function main() {
    const [, ,port] = process.argv;
    const metricsPort = parseFloat(port.toString());
     const metricsServer = new MetricsServer(metricsPort);
    await metricsServer.startServer();

}

main();
