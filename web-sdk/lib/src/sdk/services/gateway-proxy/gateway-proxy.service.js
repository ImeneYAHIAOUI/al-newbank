"use strict";
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
exports.GatewayProxyService = void 0;
const axios_1 = __importDefault(require("axios"));
const common_1 = require("@nestjs/common");
const application_not_found_exception_1 = require("../../exceptions/application-not-found.exception");
class GatewayProxyService {
    constructor(load_balancer_host) {
        this._gatewayPath = '/api/gateway/';
        this._gatewayBaseUrl = `http://${load_balancer_host}`;
    }
    getPublicKey(applicationId) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const response = yield axios_1.default.get(`${this._gatewayBaseUrl}${this._gatewayPath}/applications/${applicationId}/publickey`);
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
        });
    }
    processPayment(encryptedCardInfo) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const httpOptions = {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                };
                const response = yield axios_1.default.post(`${this._gatewayBaseUrl}${this._gatewayPath}authorize`, encryptedCardInfo, httpOptions);
                return response.data;
            }
            catch (error) {
                if (error.response && error.response.status === common_1.HttpStatus.NOT_FOUND) {
                    console.error(`Application not found`);
                    throw new application_not_found_exception_1.ApplicationNotFound();
                }
                else {
                    const errorMessage = `Error while processing payment: ${error.message}`;
                    console.error(errorMessage);
                    throw new Error(errorMessage);
                }
            }
        });
    }
}
exports.GatewayProxyService = GatewayProxyService;
