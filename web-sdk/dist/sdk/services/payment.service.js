"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.PaymentService = void 0;
const gateway_authorization_proxy_service_1 = require("./gateway-authorization-proxy/gateway-authorization-proxy.service");
const crypto = __importStar(require("crypto"));
const gateway_confirmation_proxy_service_1 = require("./gateway-confirmation-proxy/gateway-confirmation-proxy.service");
const status_reporter_proxy_service_1 = require("./status-reporter-proxy/status-reporter-proxy.service");
class PaymentService {
    constructor(retrySettings) {
        this.config = require('./config');
        this.statusReporterProxyService = new status_reporter_proxy_service_1.StatusReporterProxyService(retrySettings);
        this.gatewayAuthorizationProxyService = new gateway_authorization_proxy_service_1.GatewayAuthorizationProxyService(this.config.load_balancer_host, retrySettings, this.statusReporterProxyService);
        this.gatewayConfirmationProxyService = new gateway_confirmation_proxy_service_1.GatewayConfirmationProxyService(this.config.load_balancer_host, retrySettings, this.statusReporterProxyService);
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
    getPublicKey(token) {
        return __awaiter(this, void 0, void 0, function* () {
            return yield this.gatewayAuthorizationProxyService.getPublicKey(token);
        });
    }
    processPayment(encryptedCardInfo, token, amount) {
        return __awaiter(this, void 0, void 0, function* () {
            const payment = {
                encryptedCard: encryptedCardInfo,
                amount: amount,
            };
            console.debug('payment request sent');
            const auth = yield this.gatewayAuthorizationProxyService.authorizePaymentWithRetry(payment, token);
            console.debug('payment authorized');
            return auth;
        });
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
    authorize(paymentInfo, token) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const publicKey = yield this.getPublicKey(token);
                const encryptedCardInfo = this.encrypteCreditCard(paymentInfo, publicKey);
                const result = yield this.processPayment(encryptedCardInfo, token, paymentInfo.amount);
                return result;
            }
            catch (error) {
                throw error;
            }
        });
    }
    confirmPayment(transactionId, token) {
        return __awaiter(this, void 0, void 0, function* () {
            console.debug('payment confirmation request sent');
            try {
                return yield this.gatewayConfirmationProxyService.confirmPayment(transactionId, token);
            }
            catch (error) {
                throw error;
            }
        });
    }
    pay(paymentInfo, token) {
        return __awaiter(this, void 0, void 0, function* () {
            const auth = yield this.authorize(paymentInfo, token);
            return yield this.confirmPayment(auth.transactionId, token);
        });
    }
}
exports.PaymentService = PaymentService;
