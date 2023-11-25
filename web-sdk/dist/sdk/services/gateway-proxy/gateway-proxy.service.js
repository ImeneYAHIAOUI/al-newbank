"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.GatewayProxyService = void 0;
const axios_1 = require("axios");
const common_1 = require("@nestjs/common");
const application_not_found_exception_1 = require("../../exceptions/application-not-found.exception");
class GatewayProxyService {
    constructor(load_balancer_host) {
        this._gatewayPath = '/api/gateway/';
        this._gatewayBaseUrl = `http://${load_balancer_host}`;
    }
    async getPublicKey(token) {
        try {
            const headers = {
                Authorization: `Bearer ${token}`,
            };
            const response = await axios_1.default.get(`${this._gatewayBaseUrl}${this._gatewayPath}applications/public-key`, { headers });
            return response.data;
        }
        catch (error) {
            if (error.response && error.response.status === common_1.HttpStatus.NOT_FOUND) {
                console.error(`Application not found`);
                throw new application_not_found_exception_1.ApplicationNotFound();
            }
            else {
                const errorMessage = `Error getting public key merchant: ${error.message}`;
                console.error(errorMessage);
                throw new Error(errorMessage);
            }
        }
    }
    async processPayment(encryptedCardInfo, token) {
        try {
            const httpOptions = {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
            };
            const response = await axios_1.default.post(`${this._gatewayBaseUrl}${this._gatewayPath}authorize`, encryptedCardInfo, httpOptions);
            return response.data;
        }
        catch (error) {
            if (error.response && error.response.status === common_1.HttpStatus.NOT_FOUND) {
                console.error(error.response);
                console.error(`Application not found`);
                throw new application_not_found_exception_1.ApplicationNotFound();
            }
            else {
                const errorMessage = `Error while processing payment: ${error.message}`;
                console.error(errorMessage);
                throw new Error(errorMessage);
            }
        }
    }
    async confirmPayment(transactionId, token) {
        try {
            const httpOptions = {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
            };
            const response = await axios_1.default.post(`${this._gatewayBaseUrl}${this._gatewayPath}confirmPayment/${transactionId}`, {}, httpOptions);
            return response.data;
        }
        catch (error) {
            if (error.response && error.response.status === common_1.HttpStatus.NOT_FOUND) {
                console.error(error.response.data);
                console.error(`Application not found`);
                throw new application_not_found_exception_1.ApplicationNotFound();
            }
            else {
                const errorMessage = `Error while processing payment: ${error.message}`;
                console.error(errorMessage);
                throw new Error(errorMessage);
            }
        }
    }
}
exports.GatewayProxyService = GatewayProxyService;
//# sourceMappingURL=gateway-proxy.service.js.map