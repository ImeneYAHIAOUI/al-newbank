"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ApplicationDto = void 0;
class ApplicationDto {
    constructor(id, name, email, url, description, apiKey, merchantId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.url = url;
        this.description = description;
        this.apiKey = apiKey;
        this.merchantId = merchantId;
    }
}
exports.ApplicationDto = ApplicationDto;
