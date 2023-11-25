"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.analyticsService = void 0;
const gateway_proxy_service_1 = require("./gateway-proxy/gateway-proxy.service");
class analyticsService {
    constructor(loadBalancerHost) {
        this.gatewayProxyService = new gateway_proxy_service_1.GatewayProxyService(loadBalancerHost);
    }
}
exports.analyticsService = analyticsService;
//# sourceMappingURL=analytics.service.js.map