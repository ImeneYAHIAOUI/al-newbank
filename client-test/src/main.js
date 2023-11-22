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
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", { value: true });
// main.ts
var newbank_sdk_1 = require("@teamb/newbank-sdk");
function main() {
    return __awaiter(this, void 0, void 0, function () {
        var loadBalancerHost, _a, clientId, cardNumber, cvv, expiryDate, token, paymentService, paymentInfo, response, confirm_1, paymentService, paymentInfo, response, tokeni, confirm_2;
        return __generator(this, function (_b) {
            switch (_b.label) {
                case 0:
                    loadBalancerHost = 'localhost:80';
                    _a = process.argv, clientId = _a[2], cardNumber = _a[3], cvv = _a[4], expiryDate = _a[5], token = _a[6];
                    if (!(cardNumber && cvv && expiryDate)) return [3 /*break*/, 3];
                    paymentService = new newbank_sdk_1.PaymentService(loadBalancerHost);
                    paymentInfo = {
                        cardNumber: cardNumber,
                        cvv: cvv,
                        expirationDate: expiryDate,
                        amount: '1',
                    };
                    response = void 0;
                    return [4 /*yield*/, paymentService.authorize(paymentInfo, token)];
                case 1:
                    response = _b.sent();
                    return [4 /*yield*/, paymentService.confirmPayment(response.transactionId, token)];
                case 2:
                    confirm_1 = _b.sent();
                    console.log(confirm_1);
                    return [3 /*break*/, 6];
                case 3:
                    paymentService = new newbank_sdk_1.PaymentService(loadBalancerHost);
                    paymentInfo = {
                        cardNumber: "6176011619984148",
                        cvv: "994",
                        expirationDate: "11/2025",
                        amount: '1',
                    };
                    response = void 0;
                    tokeni = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTcwMDY1ODE3MywiaWQiOjIsIm5hbWUiOiJjb29raWVfZmFjdG9yeV9hcHBfODE4MTcwZTQiLCJlbWFpbCI6ImNvb2tpZS5mYWN0b3J5LmFwcDM3ODVAZ21haWwuY29tIiwidXJsIjoiaHR0cDovL2Nvb2tpZV9mYWN0b3J5X2FwcF84MTgxNzBlNC5jb20iLCJkZXNjcmlwdGlvbiI6IkNvb2tpZSBGYWN0b3J5IEFwcCAtIDNmMGVkZDY3IiwiZGF0ZU9mSXNzdWUiOjE3MDA2NTQ1NzMyMzl9.1dANJuFn-U-g0vynRVaLcLA8glMCDo93KNrjGnkOVCU";
                    return [4 /*yield*/, paymentService.authorize(paymentInfo, tokeni)];
                case 4:
                    response = _b.sent();
                    return [4 /*yield*/, paymentService.confirmPayment(response.transactionId, tokeni)];
                case 5:
                    confirm_2 = _b.sent();
                    console.log(confirm_2);
                    _b.label = 6;
                case 6: return [2 /*return*/];
            }
        });
    });
}
main();
