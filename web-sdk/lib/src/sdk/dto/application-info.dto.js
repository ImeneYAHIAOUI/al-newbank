"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ApplicationInfo = void 0;
class ApplicationInfo {
    constructor(name, email, IBAN, BIC, url, description) {
        this.name = name;
        this.email = email;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.url = url;
        this.description = description;
    }
}
exports.ApplicationInfo = ApplicationInfo;
