// metrics.ts
import * as promClient from 'prom-client';

export class Metrics {
    static authorizeCounter = new promClient.Counter({
        name: 'authorize_requests_total',
        help: 'Total number of authorize requests',
    });

    static authorizeFailCounter = new promClient.Counter({
        name: 'authorize_failures_total',
        help: 'Total number of authorize failures',
    });

    static confirmPaymentCounter = new promClient.Counter({
        name: 'confirm_payment_requests_total',
        help: 'Total number of confirm payment requests',
    });

    static confirmPaymentFailCounter = new promClient.Counter({
        name: 'confirm_payment_failures_total',
        help: 'Total number of confirm payment failures',
    });
}
