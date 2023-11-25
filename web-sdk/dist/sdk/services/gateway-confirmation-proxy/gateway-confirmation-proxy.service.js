"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.GatewayConfirmationProxyService = void 0;
const axios_1 = require("axios");
const common_1 = require("@nestjs/common");
const application_not_found_exception_1 = require("../../exceptions/application-not-found.exception");
class GatewayConfirmationProxyService {
    constructor(gateway_confirmation_host) {
        this._gatewayPath = '/api/gateway-confirmation';
        this._gatewayBaseUrl = `http://${gateway_confirmation_host}`;
    }
    async confirmPayment(transactionId, token) {
        try {
            const httpOptions = {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
            };
            const response = await axios_1.default.post(`${this._gatewayBaseUrl}${this._gatewayPath}/${transactionId}`, httpOptions);
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
exports.GatewayConfirmationProxyService = GatewayConfirmationProxyService;
//# sourceMappingURL=gateway-confirmation-proxy.service.js.map