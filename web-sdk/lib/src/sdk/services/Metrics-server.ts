import express from 'express';
import * as promClient from 'prom-client';
import { Metrics } from './Metrics';

export class MetricsServer {
    private app: express.Express;
    private port: number;

    constructor(port: number) {
        this.app = express();
        this.port = port;

        this.app.get('/metrics', (req, res) => {
            res.set('Content-Type', promClient.register.contentType);
            res.end(promClient.register.metrics());
        });
    }

    start(): void {
        Metrics.authorizeCounter.inc();
        Metrics.authorizeFailCounter.inc();
        Metrics.confirmPaymentCounter.inc();
        Metrics.confirmPaymentFailCounter.inc();
        Metrics.confirmPaymentFailCounter.inc();
        this.app.listen(this.port, () => {
            console.log(`Metrics server listening on port ${this.port}`);
        });
    }
}
