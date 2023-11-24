const express = require('express');
const prometheus = require('prom-client');

class MetricsServer {
  constructor(port = 5099) {
    this.app = express();
    this.metrics = this.initializeMetrics();
    this.setupRoutes();
    this.port = port;
  }

  initializeMetrics() {
    return {
      authorizeCounter: new prometheus.Counter({
        name: 'authorize_success_total',
        help: 'Total number of successful authorizations',
      }),
      authorizeFailCounter: new prometheus.Counter({
        name: 'authorize_failure_total',
        help: 'Total number of failed authorizations',
      }),
      confirmPaymentCounter: new prometheus.Counter({
        name: 'confirm_payment_success_total',
        help: 'Total number of successful payment confirmations',
      }),
      confirmPaymentFailCounter: new prometheus.Counter({
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
    return prometheus.register.metrics();
  }

  setupRoutes() {
    this.app.get('/metrics', async (req, res) => {
      try {
        res.set('Content-Type', prometheus.register.contentType);
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
    return new Promise((resolve, reject) => {
      this.server = this.app.listen(this.port, () => {
        console.log(`Server listening on port ${this.port}`);
        resolve();
      });

      this.server.on('error', (error) => {
        reject(error);
      });
    });
  }

}


