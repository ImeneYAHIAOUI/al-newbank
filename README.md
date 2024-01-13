# Team B : NewBank - V5 Merchant WebAPP SDK 

This project aims to design a cashless banking system that also supports online transaction management for partner merchants. It includes designing a Software Development Kit (SDK) to facilitate debit and credit card payments on merchants' websites.

Steps to run :
- Execute `build-all.sh` script to Load dependencies, compile if necessary, prepare the environment and build the docker containers.
- Run the `run.sh` script present in connectors/connectors/ to start the kafka-connect framework (this include the source and sink databases : redis, postgres and mongo).
- Execute `start-all.sh` script start-all.sh start the services.

# Newbank SDK

The Newbank-Merchant SDK streamlines integration with our payment system, providing developers with a clear interface to interact with payment functionalities, specifically designed for use with npm.

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
- Backend service status.

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


## Usage:

Example code can be found in the client-test/src/ directory, allowing for secure payment processing using data from a given card.

Additionally, the script ./demo.sh implements a complete usage scenario of our SDK.





