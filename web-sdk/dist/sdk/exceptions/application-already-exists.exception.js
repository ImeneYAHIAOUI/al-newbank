"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ApplicationAlreadyExists = void 0;
class ApplicationAlreadyExists extends Error {
    constructor(message = 'Application already exists') {
        super(message);
        this.name = 'Application already exists';
    }
}
exports.ApplicationAlreadyExists = ApplicationAlreadyExists;
