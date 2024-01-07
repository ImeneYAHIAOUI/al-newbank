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

## `getBackendStatus()`

Sends a request to retrieve the status of backend services.

**Return:**
- Backend service status.
