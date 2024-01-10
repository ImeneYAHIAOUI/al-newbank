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
var newbank_sdk_1 = require("@teamb/newbank-sdk");
var fs = require("fs");
var csvParser = require("csv-parser");
function processRow(loadBalancerHost, token, row) {
    return __awaiter(this, void 0, void 0, function () {
        var ClientID, CardNumber, CVV, ExpiryDate, paymentService, paymentInfo, response, confirm_1, error_1;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    ClientID = row.ClientID, CardNumber = row.CardNumber, CVV = row.CVV, ExpiryDate = row.ExpiryDate;
                    if (!(CardNumber && CVV && ExpiryDate)) return [3 /*break*/, 6];
                    paymentService = new newbank_sdk_1.PaymentService(loadBalancerHost);
                    paymentInfo = {
                        cardNumber: CardNumber,
                        cvv: CVV,
                        expirationDate: ExpiryDate,
                        amount: '1',
                    };
                    _a.label = 1;
                case 1:
                    _a.trys.push([1, 4, , 5]);
                    response = void 0;
                    return [4 /*yield*/, paymentService.authorize(paymentInfo, token)];
                case 2:
                    response = _a.sent();
                    return [4 /*yield*/, paymentService.confirmPayment(response.transactionId, token)];
                case 3:
                    confirm_1 = _a.sent();
                    console.log(confirm_1);
                    return [3 /*break*/, 5];
                case 4:
                    error_1 = _a.sent();
                    console.log(error_1);
                    return [3 /*break*/, 5];
                case 5: return [3 /*break*/, 7];
                case 6:
                    console.error('Invalid data in CSV row: ', row);
                    _a.label = 7;
                case 7: return [2 /*return*/];
            }
        });
    });
}
function main() {
    return __awaiter(this, void 0, void 0, function () {
        var loadBalancerHost, token, data;
        var _this = this;
        return __generator(this, function (_a) {
            loadBalancerHost = 'localhost:5060';
            token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTg0NDI3NCwiaWQiOjE2LCJuYW1lIjoiYXBwbGkiLCJlbWFpbCI6Inl1dHV5LWZnQGppby5jb20iLCJ1cmwiOiJocG9wXmxyZHJ0cHBzcHVhYXl0ZXRpaCIsImRlc2NyaXB0aW9uIjoiZHlydHNyZnVoayIsImRhdGVPZklzc3VlIjoxNjk5ODQwNjc0MDcwfQ.-E1IXGNNrk_CMIgNoY6jRO5w0ddgPJV3V-5yDY6m7-Q";
            data = [];
            fs.createReadStream('./client_cards.csv')
                .pipe(csvParser())
                .on('data', function (row) {
                data.push(row);
            })
                .on('end', function () { return __awaiter(_this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: 
                        // Use Promise.all to parallelize the processing of each row
                        return [4 /*yield*/, Promise.all(data.map(function (row) { return processRow(loadBalancerHost, token, row); }))];
                        case 1:
                            // Use Promise.all to parallelize the processing of each row
                            _a.sent();
                            console.log('All rows processed');
                            return [2 /*return*/];
                    }
                });
            }); });
            return [2 /*return*/];
        });
    });
}
main();
