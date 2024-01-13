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
exports.GatewayConfirmationProxyService = void 0;
const axios_1 = __importDefault(require("axios"));
const retry = __importStar(require("retry"));
const common_1 = require("@nestjs/common");
const application_not_found_exception_1 = require("../../exceptions/application-not-found.exception");
const internal_server_exception_1 = require("../../exceptions/internal-server.exception");
const unauthorized_exception_1 = require("../../exceptions/unauthorized.exception");
const metrics_proxy_1 = require("../metrics-proxy/metrics-proxy");
const request_dto_1 = require("../../dto/request.dto");
class GatewayConfirmationProxyService {
    constructor(load_balancer_host, retrySettings, statusReporterProxyService) {
        this.config = require('./../config');
        this._gatewayPath = '/api/gateway-confirmation';
        this._gatewayBaseUrl = `${load_balancer_host}`;
        this.retrySettings = retrySettings;
        this.metricsProxy = new metrics_proxy_1.MetricsProxy(retrySettings);
        this.statusReporterProxyService = statusReporterProxyService;
    }
    confirmPayment(transactionId, token) {
        const operation = retry.operation({
            retries: this.retrySettings.retries,
            factor: this.retrySettings.factor,
            minTimeout: this.retrySettings.minTimeout,
            maxTimeout: this.retrySettings.maxTimeout,
            randomize: this.retrySettings.randomize,
        });
        let lastError;
        const start = new Date().getTime();
        return new Promise((resolve, reject) => {
            operation.attempt((currentAttempt) => __awaiter(this, void 0, void 0, function* () {
                try {
                    const httpOptions = {
                        headers: {
                            'Content-Type': 'application/json',
                            Authorization: `Bearer ${token}`,
                        },
                    };
                    yield this.statusReporterProxyService.isServiceAvailable(this.config.service_confirmation_name);
                    const response = yield axios_1.default.post(`${this._gatewayBaseUrl}${this._gatewayPath}/${transactionId}`, Object.assign(Object.assign({}, httpOptions), { timeout: this.config.maxTimeOut }));
                    resolve(response.data);
                    const end = new Date().getTime();
                    const time = end - start;
                    const request = new request_dto_1.RequestDto(new Date().toISOString(), time, 'SUCCESS', 'Payment confirmed');
                    yield this.metricsProxy.sendRequestResult(request, token);
                }
                catch (error) {
                    lastError = error;
                    const end = new Date().getTime();
                    const time = end - start;
                    if (operation.retry(lastError)) {
                        console.error(`Retry attempt ${currentAttempt} failed. Retrying...`);
                        return;
                    }
                    console.error(`All retry attempts failed. Last error: ${lastError === null || lastError === void 0 ? void 0 : lastError.message}`);
                    if (isAxiosError(lastError) && lastError.response) {
                        if (lastError.response.status === common_1.HttpStatus.UNAUTHORIZED) {
                            console.error(lastError.response);
                            console.error(`Unauthorized access`);
                            reject(new unauthorized_exception_1.UnauthorizedError(lastError.message));
                        }
                        else if (lastError.response.status === common_1.HttpStatus.NOT_FOUND) {
                            console.error(lastError.response);
                            console.error(`Application not found`);
                            reject(new application_not_found_exception_1.ApplicationNotFound(lastError.response));
                        }
                        else if (lastError.response.status === common_1.HttpStatus.INTERNAL_SERVER_ERROR) {
                            console.error(lastError.response);
                            reject(new internal_server_exception_1.InternalServerError(lastError.message));
                        }
                    }
                    else {
                        const errorMessage = `Error while processing payment: ${lastError === null || lastError === void 0 ? void 0 : lastError.message}`;
                        console.error(errorMessage);
                        reject(new Error(errorMessage));
                    }
                    const request = new request_dto_1.RequestDto(new Date().toISOString(), time, 'FAILED', 'Payment confirmation failed');
                    yield this.metricsProxy.sendRequestResult(request, token);
                }
            }));
        });
    }
}
exports.GatewayConfirmationProxyService = GatewayConfirmationProxyService;
function isAxiosError(error) {
    return error.isAxiosError === true && error.response !== undefined;
}
