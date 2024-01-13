"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.MerchantAlreadyExists = void 0;
class MerchantAlreadyExists extends Error {
    constructor(message = 'Merchant already exists') {
        super(message);
        this.name = 'Merchant already exists';
    }
}
exports.MerchantAlreadyExists = MerchantAlreadyExists;
