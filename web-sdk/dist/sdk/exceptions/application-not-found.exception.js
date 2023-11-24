"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ApplicationNotFound = void 0;
class ApplicationNotFound extends Error {
    constructor(message = 'Application not found') {
        super(message);
        this.name = 'Application not found';
    }
}
exports.ApplicationNotFound = ApplicationNotFound;
//# sourceMappingURL=application-not-found.exception.js.map