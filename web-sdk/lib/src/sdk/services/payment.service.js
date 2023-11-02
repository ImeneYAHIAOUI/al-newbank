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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.PaymentService = void 0;
const gateway_proxy_service_1 = require("./gateway-proxy/gateway-proxy.service");
const axios_1 = __importDefault(require("axios"));
const crypto = __importStar(require("crypto"));
class PaymentService {
    constructor(loadBalancerHost) {
        this.gatewayProxyService = new gateway_proxy_service_1.GatewayProxyService(loadBalancerHost);
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
    retrieveLocation() {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const ipInfoResponse = yield axios_1.default.get('https://ipinfo.io');
                const location = ipInfoResponse.data.loc;
                if (!location) {
                    throw new Error('No location information available');
                }
                console.debug('Location:', location);
                return location;
            }
            catch (error) {
                throw new Error('Error retrieving location information: ' + error.message);
            }
        });
    }
    processCardInfo(paymentInfo, applicationId, token) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                this.validateCardInfo(paymentInfo);
                const publicKey = yield this.gatewayProxyService.getPublicKey(applicationId);
                console.debug('Public key:', publicKey);
                const cardInfo = {
                    cardNumber: paymentInfo.cardNumber,
                    expirationDate: paymentInfo.expirationDate,
                    cvv: paymentInfo.cvv,
                };
                console.debug('Card info:', Buffer.from(JSON.stringify(cardInfo)));
                const plaintext = `${paymentInfo.cardNumber.toString()},${paymentInfo.expirationDate.toString()},${paymentInfo.cvv.toString()}`;
                console.debug('Plaintext:', plaintext);
                const buffer = Buffer.from(plaintext, 'utf8');
                const encryptedCardInfo = crypto.publicEncrypt('-----BEGIN PUBLIC KEY-----\n' +
                    publicKey +
                    '\n-----END PUBLIC KEY-----', buffer);
                const payment = {
                    cryptedCreditCard: encryptedCardInfo.toString('base64'),
                    amount: paymentInfo.amount,
                    token: token,
                };
                console.debug('Payment:', payment);
                yield this.gatewayProxyService.processPayment(JSON.stringify(payment));
                return encryptedCardInfo;
            }
            catch (error) {
                throw new Error('Error processing card information: ' + error.message);
            }
        });
    }
}
exports.PaymentService = PaymentService;
