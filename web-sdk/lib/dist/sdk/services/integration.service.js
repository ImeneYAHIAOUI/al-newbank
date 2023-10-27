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
Object.defineProperty(exports, "__esModule", { value: true });
exports.IntegrationService = void 0;
const invalid_merchant_information_exception_1 = require("../exceptions/invalid-merchant-information.exception");
const invalid_application_information_exception_1 = require("../exceptions/invalid-application-information.exception");
const gateway_proxy_service_1 = require("./gateway-proxy/gateway-proxy.service");
class IntegrationService {
    constructor(loadBalancerHost) {
        this.gatewayService = new gateway_proxy_service_1.GatewayProxyService(loadBalancerHost);
    }
    validateMerchantInfo(name, email, iban, bic) {
        if (!name || !email || !iban || !bic) {
            throw new invalid_merchant_information_exception_1.InvalidMerchantInformationException('Name, email, IBAN, and BIC are required.');
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            throw new invalid_merchant_information_exception_1.InvalidMerchantInformationException('Invalid email address.');
        }
        const ibanRegex = /^[A-Z]{2}\d{2}[A-Z0-9]{4}\d{7}([A-Z0-9]?){0,16}$/;
        if (!ibanRegex.test(iban)) {
            throw new invalid_merchant_information_exception_1.InvalidMerchantInformationException('Invalid IBAN.');
        }
        const bicRegex = /^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$/;
        if (!bicRegex.test(bic)) {
            throw new invalid_merchant_information_exception_1.InvalidMerchantInformationException('Invalid BIC.');
        }
    }
    integrateMerchant(name, email, iban, bic) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                this.validateMerchantInfo(name, email, iban, bic);
                const merchant = { name, email, bankAccount: { iban: iban, bic: bic } };
                return yield this.gatewayService.integrateMerchant(merchant);
            }
            catch (error) {
                console.error(`Error in integrating merchant: ${error.message}`);
                throw error;
            }
        });
    }
    validateApplicationInfo(name, email, url, description, merchantId) {
        if (!name || !email || !url || !description || !merchantId) {
            throw new invalid_application_information_exception_1.InvalidApplicationInformationException('All fields are required.');
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            throw new invalid_application_information_exception_1.InvalidApplicationInformationException('Invalid email address.');
        }
    }
    integrateApplication(name, email, url, description, merchantId) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                this.validateApplicationInfo(name, email, url, description, merchantId);
                const integratedApplication = {
                    name,
                    email,
                    url,
                    description,
                    merchantId,
                };
                return yield this.gatewayService.integrateApplication(integratedApplication);
            }
            catch (error) {
                console.error(`Error in integrating application: ${error.message}`);
                throw error;
            }
        });
    }
    integrateService(application) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const merchantIntegrationResult = yield this.integrateMerchant(application.name, application.email, application.IBAN, application.BIC);
                console.log(`Merchant integration successful for ${merchantIntegrationResult.name}. Merchant ID: ${merchantIntegrationResult.id}`);
                const applicationIntegrationResult = yield this.integrateApplication(merchantIntegrationResult.name, merchantIntegrationResult.email, application.url, application.description, merchantIntegrationResult.id.toString());
                const applicationId = applicationIntegrationResult.id.toString();
                console.log(`Application integration successful for ${application.name}. Application ID: ${applicationId}`);
                return {
                    id: applicationId,
                    apiKey: applicationIntegrationResult.apiKey.toString(),
                };
            }
            catch (error) {
                if (error instanceof invalid_merchant_information_exception_1.InvalidMerchantInformationException) {
                    console.error(`Invalid Merchant Information: ${error.message}`);
                }
                else if (error instanceof invalid_application_information_exception_1.InvalidApplicationInformationException) {
                    console.error(`Invalid Application Information: ${error.message}`);
                }
                console.error(`Error in integrating service: ${error.message}`);
                throw error;
            }
        });
    }
}
exports.IntegrationService = IntegrationService;
