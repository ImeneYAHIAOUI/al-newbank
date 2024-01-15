"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AppService = void 0;
const common_1 = require("@nestjs/common");
const newbank_sdk_1 = require("@teamb/newbank-sdk");
const newbank_sdk_2 = require("@teamb/newbank-sdk");
let AppService = class AppService {
    constructor() {
        const retrySettings = new newbank_sdk_1.RetrySettings({
            retries: Number(process.env.RETRIES) || 2,
            factor: Number(process.env.FACTOR) || 2,
            minTimeout: Number(process.env.MIN_TIMEOUT) || 1000,
            maxTimeout: Number(process.env.MAX_TIMEOUT) || 3000,
            randomize: Boolean(process.env.RANDOMIZE) || true,
        });
        const token = process.env.NEWBANK_TOKEN;
        this.newbankSdk = new newbank_sdk_1.NewbankSdk(token, retrySettings);
    }
    async payment(paymentInfoDTO) {
        try {
            const result = await this.newbankSdk.authorizePayment(paymentInfoDTO);
            await this.newbankSdk.confirmPayment(result.transactionId);
        }
        catch (error) {
            if (error instanceof newbank_sdk_2.ServiceUnavailableException) {
                console.log(error.message);
                const regex = /\d+$/;
                const match = (error.message).match(regex);
                if (match) {
                    const timeToSleeping = parseInt(match[0], 10);
                    const start = new Date().getTime();
                    const delayMilliseconds = timeToSleeping * 1000;
                    while (new Date().getTime() - start < delayMilliseconds) {
                    }
                }
            }
            else {
                console.log(error.message);
            }
        }
    }
    pay(paymentInfoDTO) {
        this.newbankSdk.pay(paymentInfoDTO)
            .then(r => console.log(r))
            .catch(error => {
            console.error(error.message);
        });
    }
    authorizePayment(paymentInfoDTO) {
        this.newbankSdk.authorizePayment(paymentInfoDTO)
            .then(r => console.log(r))
            .catch(error => {
            console.error(error.message);
        });
    }
    confirmPayment(transactionId) {
        this.newbankSdk.confirmPayment(transactionId)
            .then(r => console.log(r))
            .catch(error => {
            console.error(error.message);
        });
    }
    getBackendStatus() {
        return this.newbankSdk.getBackendStatus();
    }
    getMetrics(body) {
        return this.newbankSdk.getMetrics(body);
    }
    getHello() {
        return 'Hello World!';
    }
};
exports.AppService = AppService;
exports.AppService = AppService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [])
], AppService);
//# sourceMappingURL=app.service.js.map