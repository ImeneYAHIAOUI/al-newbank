declare const express: any;
declare const prometheus: any;
declare class MetricsServer {
    constructor(port?: number);
    initializeMetrics(): {
        authorizeCounter: any;
        authorizeFailCounter: any;
        confirmPaymentCounter: any;
        confirmPaymentFailCounter: any;
    };
    authorizeSuccess(): void;
    authorizeFailure(): void;
    confirmPaymentSuccess(): void;
    confirmPaymentFailure(): void;
    getFormattedMetrics(): any;
    setupRoutes(): void;
    startServer(): Promise<unknown>;
}
