# Newbank SDK

Newbank SDK is an SDK provided by Newbank-teamb for use by developers of merchants wishing to integrate our PaymentGateway service to their website and use it for the transactions done by their clients.

## Installation

**Pre-requists** 
In order to use the SDK Download and install Node.js and npm from [here](https://nodejs.org/en/download/).

Then run the following command : 
```bash
npm install @teamb/newbank-sdk
```

## Initialisation

To use the SDK you should join Newbank by integrating your business application to get your api token from our service. 

To start working with the SDK instantiate the NewBank client and provide the token you've just been provided.

```JS
import {NewbankSdk} from "@teamb/newbank-sdk";

const token = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" // your business api token
const newbankSdk = new NewbankSdk(token);
```

## API Interface

### `authorizePayment(paymentInformation)`

Sends a payment authorization request to the backend and receives a transaction ID if accepted.

```JS
import {PaymentInfoDTO} from "@teamb/newbank-sdk";
import {AuthorizeDto} from "@teamb/newbank-sdk";

const paymentInfo: PaymentInfoDTO = {
    cardNumber: cardNumber,
    cvv: cvv,
    expirationDate: expiryDate,
    amount: '500',
};
response: AuthorizeDto = await newbankSdk.authorizePayment(paymentInfo);
```

**Parameters:**
- `paymentInformation`: Payment information (credit card, amount, etc.).

**Return:**
- `transactionId`: The transaction ID if the request is accepted.

### `confirmPayment(transactionId)`

Sends a payment confirmation request for the previously authorized transaction.


```JS
import {PaymentInfoDTO} from "@teamb/newbank-sdk";
import {AuthorizeDto} from "@teamb/newbank-sdk";

const confirmationMessage = await newbankSdk.confirmPayment(response.transactionId);
console.log(confirmationMessage);
```
**Return:**

- `response` : A message indicating whether the transaction is confirmed or not.

**Parameters:**
- `transactionId`: The ID of the transaction to be confirmed.

### `pay(paymentInformation)`

Includes the steps of sending a payment authorization request to the backend via the `authorizePayment(paymentInformation)` method and, if accepted, confirms the transaction using the `confirmPayment(transactionId)` function.

```JS
import {PaymentInfoDTO} from "@teamb/newbank-sdk";
import {AuthorizeDto} from "@teamb/newbank-sdk";

const paymentInfo: PaymentInfoDTO = {
          cardNumber: cardNumber,
          cvv: cvv,
          expirationDate: expiryDate,
          amount: '500',
};
const response = await newbankSdk.pay(paymentInfo);
console.log(response);
```


**Parameters:**
- `paymentInformation`: Payment information.

**Return:**
- `response`: A message indicating whether the transaction is confirmed or not.

### `getBackendStatus()`

Sends a request to retrieve the status of backend services.

```JS
import {BackendStatusDto} from "@teamb/newbank-sdk";

const backendStatus: BackendStatusDto = await newbankSdk.getBackendStatus();

console.log(JSON.stringify(backendStatus));
```

**Return:**
- a json list with each element containing the following fields:
  - `name`: The name of the backend service.
  - `status`: The status of the backend service. It can be either `1` (up), `2` (down) or `3` (degraded).

### `getMetrics(metricsQuery)`
Sends a request to retrieve the metrics of the payment website.

```JS
import {MetricRequestDto} from "@teamb/newbank-sdk";

const metricsQuery: MetricRequestDto = {
    period: "L6H",
    resolution: "5M",
    metrics: [
        "transactionCount",
        "TransactionSuccessRate",
        "TransactionFailureRate",
        "totalAmountSpent"
    ],
    filters: {
        status: ["AUTHORISED", "CONFIRMED"],
        creditCardType: ["credit"]
    }
};

const metrics = await newbankSdk.getMetrics(metricsQuery);
console.log(JSON.stringify(metrics));
```



**Parameters:**
- `metricsQuery`: Object containing the metrics query.
     
#### restrictions:
- `period` or `timeRange` must be specified.
- `period` and `timeRange` are mutually exclusive. if both are specified, `timeRange` is ignored.
- `resolution` must be specified and must be one of the following values:
   - `5M` (5 minutes).
   - `15M` (15 minutes).
   - `30M` (30 minutes).
   - `H` (1 hour).
   - `D` (1 day).
   - `W` (1 week).
   - `M` (1 month).
   - `Y` (1 year).
- `metrics` must be a list of one or more of the following values:
   - `transactionCount`: The number of transactions.
   - `TransactionSuccessRate`: The percentage of successful transactions.
   - `TransactionFailureRate`: The percentage of failed transactions.
   - `totalAmountSpent`: The total amount spent.
   - `averageAmountSpent`: The average amount spent per transaction.
   - `totalFees`: The total fees paid.
   - `averageFees`: The average fees paid per transaction.
   - `totalRequestsCount`: The total number of requests sent from the web service to the payment gateway.
   - `successfulRequestsCount`: The number of successful requests sent from the web service to the payment gateway.
   - `failedRequestsCount`: The number of failed requests sent from the web service to the payment gateway.
   - `successfulRequestsRate` : The percentage of successful requests sent from the web service to the payment gateway.
   - `failedRequestsRate` : The percentage of failed requests sent from the web service to the payment gateway.
   - `averageRequestTime`: The average time taken by the payment gateway to process a request.
- `filters` must be a list of one or more of the following values:
  - `status`: By passing the filter, only the metrics concerning the transactions with the specified status are returned. The possible values are:
     - `AUTHORISED`
     - `CONFIRMED`
     - `FAILED`
     - `PENDING_SETTLEMENT`
     - `SETTLED`
  - `creditCardType`: By passing the filter, only the metrics concerning the transactions with the specified credit card type are returned. This filter takes only one value. If multiple values are passed, only the first one is considered. The possible values are:
     - `credit`
     - `debit`
  
The client may choose how to visualize the data returned by the `getBackenStatus()` and `getMetrics(metricsQuery)` methods.
Grafana dashboards examples and a setup guide can be found [here](./Grafana).
     
**Return:**
- `metrics`: A list of metrics with their values and timestamps.


### `Retry policies`

Payment calls can be retried using an exponential backoff strategy. 

```JS
import {NewbankSdk, RetrySettings} from "@teamb/newbank-sdk";

const retrySettings = new RetrySettings({ retries: 2,
                                          factor:2,
                                          minTimeout: 1000,
                                          maxTimeout: 3000,
                                          randomize: true });
const token = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" 
const newbankSdk = new NewbankSdk(token, retrySettings);
```
The retrial concerns all subsequent calls made by the SDK.

The RetrySettings class provides a convenient way to configure retry behavior :
   
   `retries`: The maximum number of retry attempts. Default is `3`.
   
   `factor`: The exponential factor to determine the delay between retries. Default is `2`.
   
   `minTimeout`: The minimum time (in milliseconds) to wait before the first retry. Default is `1000`.
   
   `maxTimeout`: The maximum time (in milliseconds) between two retry attempts. Default is `3000`.
   
   `randomize`: A boolean indicating whether to randomize the timeouts. Default is `true`.

