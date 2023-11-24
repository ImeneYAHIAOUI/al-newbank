import express, { Application } from 'express';
import { Counter, register } from 'prom-client';

export class MetricsServer {
  private readonly app: Application;
  private readonly metrics: {
    authorizeCounter: Counter;
    authorizeFailCounter: Counter;
    confirmPaymentCounter: Counter;
    confirmPaymentFailCounter: Counter;
  };
  private server: any; // Type de server défini plus bas
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

  getFormattedMetrics() {
    return register.metrics();
  }

  setupRoutes() {
    this.app.get('/metrics', async (req, res) => {
      try {
        res.set('Content-Type', register.contentType);
        const metricsData = await this.getFormattedMetrics();
        res.end(metricsData);
      } catch (error) {
        console.error('Error while getting metrics:', error);
        res.status(500).end('Internal Server Error');
      }
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
  }

  startServer() {
    return new Promise<void>((resolve, reject) => {
      this.server = this.app.listen(this.port, () => {
        console.log(`Server listening on port ${this.port}`);
        resolve();
      });

      this.server.on('error', (error: any) => {
        reject(error);
      });
    });
  }
}

