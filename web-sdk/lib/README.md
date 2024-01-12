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

**Parameters:**
- `paymentInformation`: Payment information (credit card, amount, etc.).

**Return:**
- `transactionId`: The transaction ID if the request is accepted.

### `confirmPayment(transactionId)`

Sends a payment confirmation request for the previously authorized transaction.

**Parameters:**
- `transactionId`: The ID of the transaction to be confirmed.

### `pay(paymentInformation)`

Includes the steps of sending a payment authorization request to the backend via the `authorizePayment(paymentInformation)` method and, if accepted, confirms the transaction using the `confirmPayment(transactionId)` function.

**Parameters:**
- `paymentInformation`: Payment information.

### `getBackendStatus()`

Sends a request to retrieve the status of backend services.

**Return:**
- a json list with each element containing the following fields:
  - `name`: The name of the backend service.
  - `status`: The status of the backend service. It can be either `1` (up), `2` (down) or `3` (degraded).

### `getMetrics(metricsQuery)`
Sends a request to retrieve the metrics of the payment website.

**Parameters:**
- metricsQuery: : Json object containing the following fields:
#### Obligatory fields:
- `period or timeRange`: 
  - `period`: Period of the metrics query. Starts with `L` (last) followed by a number and a one of the following values :
    - `MI`(minutes)
    - `H` (hours)
    - `D` (days),
    - `M` (months) 
    - `Y` (years).
      
    For example, `L6H` means last 6 hours, `L1M` means last month, `L2Y` means last 2 years.

  - `timeRange`: a json sub-object containing the fields `from` and `to` which are the start and end dates of the metrics query. it accepts the following date formats:
    - Date and Time with Seconds (ISO-8601): `YYYY-MM-DDTHH:MM:SSZ` (2024-01-12T12:34:56)
    - Date and Time with fractional seconds (ISO-8601): `YYYY-MM-DDTHH:MM:SS.sssZ` (2024-01-12T12:34:56.789Z)
    - Date only (ISO-8601): `YYYY-MM-DD` (2024-01-12)
    - Date and Time with Offset (ISO-8601): `YYYY-MM-DDTHH:MM:SS+HH:MM` (2024-01-12T12:34:56+01:00)
  - `resolution`: Specifies the time interval between two consecutive data points. It can be one of the following values: 
    - `5M` (5 minutes)
    - `15M` (15 minutes)
    - `30M` (30 minutes)
    - `H` (1 hour)
    - `D` (1 day)
    - `W` (1 week)
    - `M` (1 month)
    - `Y` (1 year).
  
#### Example of a valid minimalistic metrics query with the period field:
```json
{
    "period": "L6H",
    "resolution": "5M"
}
```

#### Example of a valid minimalistic metrics query with the timeRange field:
```json
{
    "timeRange": {
        "from": "2024-01-12T12:34:56+01:00",
        "to": "2024-01-12T17:34:56+01:00"
    },
    "resolution": "5M"
}
```

By default, the metrics query returns the following metrics:

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
- `averageRequestTime`: The average time taken by the payment gateway to process a request.

#### Optional fields:

- `metrics`: A list of metrics to be returned. It can be one or more of the metrics listed above. If not specified, all metrics are returned.
- `filters` : A set of filters to be applied to the metrics query, each filter has a key (filter name) and a list of values. The following filters are available:
  - `status`: By passing the filter, only the metrics concerning the transactions with the specified status are returned. The possible values are:
    - `AUTHORISED`
    - `CONFIRMED`
    - `FAILED`
    - `PENDING_SETTLEMENT`
    - `SETTLED`
  - `creditCardType`: By passing the filter, only the metrics concerning the transactions with the specified credit card type are returned. This filter takes only one value. If multiple values are passed, only the first one is considered. The possible values are:
    - `credit`
    - `debit`

#### Example of a valid metrics query with all the fields:
```json
{
    "period": "L6H",
    "resolution": "5M",
    "metrics": [
        "transactionCount",
        "TransactionSuccessRate",
        "TransactionFailureRate",
        "totalAmountSpent"
    ],
    "filters": {
        "status": [
            "AUTHORISED",
            "CONFIRMED"
        ],
        "creditCardType": [
            "credit"
        ]
    }
}
```
             
     
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

