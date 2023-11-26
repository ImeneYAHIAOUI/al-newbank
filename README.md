# Team B : NewBank - V5 Merchant WebAPP SDK 

This project aims to design a cashless banking system that also supports online transaction management for partner merchants. It includes designing a Software Development Kit (SDK) to facilitate debit and credit card payments on merchants' websites.

* Steps to run :
Execute `build-all.sh` script to Load dependencies, compile if necessary, prepare the environment and build the docker containers.
Execute `start-all.sh` script start-all.sh start the services.
## Newbank-Merchant SDK:
The Newbank-Merchant SDK streamlines integration with our payment system, providing developers with a clear interface to interact with payment functionalities, specifically designed for use with npm.

* Retry policiers

Payment calls will be retried using an exponential backoff strategy. The RetrySettings class provides a convenient way to configure retry behavior :
   
   `retries`: The maximum number of retry attempts. Default is `3`.
   `factor`: The exponential factor to determine the delay between retries. Default is `2`.
   `minTimeout`: The minimum time (in milliseconds) to wait before the first retry. Default is `1000`.
   `maxTimeout`: The maximum time (in milliseconds) between two retry attempts. Default is `3000`.
   `randomize`: A boolean indicating whether to randomize the timeouts. Default is `true`.


* Prerequisites:

Download and install Node.js and npm from here.

* Installation:

Use npm to install our SDK: npm install @teamb/newbank-sdk

* Usage:

An example code can be found in the client-test/src/ directory, allowing for secure payment processing using data from a given card.

Each merchant must first register on our platform with their application to obtain an API key, which developers will use when integrating our SDK with their corresponding website.

Additionally, the script ./demo.sh implements a complete usage scenario of our SDK.





