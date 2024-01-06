"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.PaymentService = void 0;
const gateway_proxy_service_1 = require("./gateway-proxy/gateway-proxy.service");
const crypto = require("crypto");
const gateway_confirmation_proxy_service_1 = require("./gateway-confirmation-proxy/gateway-confirmation-proxy.service");
const metrics_reporter_1 = require("./metrics-reporter");
class PaymentService {
    constructor(loadBalancerHost, metricsPort) {
        this.gatewayProxyService = new gateway_proxy_service_1.GatewayProxyService(loadBalancerHost);
        this.gatewayConfirmationProxyService = new gateway_confirmation_proxy_service_1.GatewayConfirmationProxyService('localhost:5070');
        this.metricsReporter = new metrics_reporter_1.MetricsReporter(`http://localhost:${metricsPort}`, metricsPort);
    }
    validateCardInfo(paymentInfo) {
        if (!paymentInfo ||
            !paymentInfo.cardNumber ||
            !paymentInfo.expirationDate ||
            !paymentInfo.cvv) {
            throw new Error('Invalid card information');
        }
        const cardNumberRegex = /^\d{16}$/;
        if (!cardNumberRegex.test(paymentInfo.cardNumber)) {
            throw new Error('Invalid card number. It should be a 16-digit number.');
        }
        const expirationDateRegex = /^(0[1-9]|1[0-2])\/\d{4}$/;
        if (!expirationDateRegex.test(paymentInfo.expirationDate)) {
            throw new Error('Invalid expiration date. It should be in the format MM/YYYY.');
        }
        const cvvRegex = /^\d{3}$/;
        if (!cvvRegex.test(paymentInfo.cvv)) {
            throw new Error('Invalid CVV. It should be a 3-digit number.');
        }
    }
    async getPublicKey(token) {
        return await this.gatewayProxyService.getPublicKey(token);
    }
    async processPayment(encryptedCardInfo, token, amount) {
        try {
            const payment = {
                encryptedCard: encryptedCardInfo,
                amount: amount,
            };
            console.debug('payment request sent');
            const auth = await this.gatewayProxyService.processPayment(payment, token);
            console.debug('payment authorized');
            return auth;
        }
        catch (error) {
            throw new Error('Error processing card information: ' + error.message);
        }
    }
    encrypteCreditCard(paymentInfo, publicKey) {
        this.validateCardInfo(paymentInfo);
        const cardInfo = {
            cardNumber: paymentInfo.cardNumber,
            expirationDate: paymentInfo.expirationDate,
            cvv: paymentInfo.cvv,
        };
        const plaintext = `${paymentInfo.cardNumber.toString()},${paymentInfo.expirationDate.toString()},${paymentInfo.cvv.toString()}`;
        const buffer = Buffer.from(plaintext, 'utf8');
        const encryptedCardInfo = crypto.publicEncrypt('-----BEGIN PUBLIC KEY-----\n' +
            publicKey +
            '\n-----END PUBLIC KEY-----', buffer);
        return encryptedCardInfo.toString('base64');
    }
    async authorize(paymentInfo, token) {
        try {
            const publicKey = await this.getPublicKey(token);
            const encryptedCardInfo = this.encrypteCreditCard(paymentInfo, publicKey);
            const result = await this.processPayment(encryptedCardInfo, token, paymentInfo.amount);
            this.metricsReporter.sendPostRequest('/authorize/success');
            return result;
        }
        catch (error) {
            this.metricsReporter.sendPostRequest('/authorize/failure');
            throw error;
        }
    }
    async confirmPayment(transactionId, token) {
        console.debug('payment confirmation request sent');
        try {
            const result = await this.gatewayConfirmationProxyService.confirmPayment(transactionId, token);
            this.metricsReporter.sendPostRequest('/confirm/payment/success');
            return result;
        }
        catch (error) {
            this.metricsReporter.sendPostRequest('/confirm/payment/failure');
            throw error;
        }
    }
    async pay(paymentInfo, token) {
        const auth = await this.authorize(paymentInfo, token);
        return await this.confirmPayment(auth.transactionId, token);
    }
}
exports.PaymentService = PaymentService;
//# sourceMappingURL=payment.service.js.map