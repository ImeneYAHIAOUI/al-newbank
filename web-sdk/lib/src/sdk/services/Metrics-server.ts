import express, { Application } from 'express';
import { Counter, register } from 'prom-client';
import * as http from 'http';

export class MetricsServer {
  private readonly app: Application;
  private readonly metrics: {
    authorizeCounter: Counter;
    authorizeFailCounter: Counter;
    confirmPaymentCounter: Counter;
    confirmPaymentFailCounter: Counter;
  };
  private server!: http.Server;
  private readonly port: number;

  constructor(port = 5099) {
    this.app = express();
    this.metrics = this.initializeMetrics();
    this.setupRoutes();
    this.port = port;
  }

  private initializeMetrics() {
    return {
      authorizeCounter: new Counter({
        name: 'authorize_success_total',
        help: 'Total number of successful authorizations',
      }),
      authorizeFailCounter: new Counter({
        name: 'authorize_failure_total',
        help: 'Total number of failed authorizations',
      }),
      confirmPaymentCounter: new Counter({
        name: 'confirm_payment_success_total',
        help: 'Total number of successful payment confirmations',
      }),
      confirmPaymentFailCounter: new Counter({
        name: 'confirm_payment_failure_total',
        help: 'Total number of failed payment confirmations',
      }),
    };
  }

  authorizeSuccess() {
    this.metrics.authorizeCounter.inc();
  }

  authorizeFailure() {
    this.metrics.authorizeFailCounter.inc();
  }

  confirmPaymentSuccess() {
    this.metrics.confirmPaymentCounter.inc();
  }

  confirmPaymentFailure() {
    this.metrics.confirmPaymentFailCounter.inc();
  }

getFormattedMetrics(): Promise<string> {
    return new Promise((resolve, reject) => {
      register.metrics().then(metricsData => {
        resolve(metricsData);
      }).catch(error => {
        reject(error);
      });
    });
  }

  setupRoutes(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.app.get('/metrics', async(req, res) => {
        res.set('Content-Type', register.contentType);
        const metricsData = await this.getFormattedMetrics();
        res.end(metricsData);
      });

      this.app.post('/authorize/success', (req, res) => {
        this.authorizeSuccess();
        res.send('Authorization successful');
      });

      this.app.post('/authorize/failure', (req, res) => {
        this.authorizeFailure();
        res.send('Authorization failed');
      });

      this.app.post('/confirm/payment/success', (req, res) => {
        this.confirmPaymentSuccess();
        res.send('Payment confirmation successful');
      });

      this.app.post('/confirm/payment/failure', (req, res) => {
        this.confirmPaymentFailure();
        res.send('Payment confirmation failed');
      });

      this.app.use((err: any, req: express.Request, res: express.Response, next: express.NextFunction) => {
        console.error('Error in request:', err);
        res.status(500).send('Internal Server Error');
      });

      resolve();
    });
  }

  startServer(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.server = this.app.listen(this.port, () => {
        console.log(`Metrics server listening on port ${this.port}`);
        resolve();
      });

      this.server.on('error', (error: any) => {
        reject(error);
      });
    });
  }

  stopServer(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (this.server) {
        this.server.close((err: any) => {
          if (err) {
            reject(err);
          } else {
            console.log('Server stopped');
            resolve();
          }
        });
      } else {
        resolve();
      }
    });
  }
}



