# Team B : NewBank - V5 Merchant WebAPP SDK 

This project aims to design a cashless banking system that also supports online transaction management for partner merchants. It includes designing a Software Development Kit (SDK) to facilitate debit and credit card payments on merchants' websites.

Steps to run :
- Execute `build-all.sh` script to Load dependencies, compile if necessary, prepare the environment and build the docker containers.
- Run the `run.sh` script present in connectors/connectors/ to start the kafka-connect framework (this include the source and sink databases : redis, postgres and mongo).
- Execute `start-all.sh` script start-all.sh start the services.

# Newbank SDK

The Newbank-Merchant SDK streamlines integration with our payment system, providing developers with a clear interface to interact with payment functionalities, specifically designed for use with npm.

## Documentation

Documentation on how to install the SDK and use its API interface can be found [here](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/web-sdk/README.md).

Also, you can fund in this [link](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/web-sdk/Grafana/GrafanaSetUp.md) how to set up Grafana Dashboard for your web service Metrics.

## Usage

An example of a NestJS project using our SDK can be found in the **web-sdk-example/nestjs-web-service** directory. This example demonstrates secure payment processing using data from a given card.

Additionally, the script `run-all-tests.sh` in the **web-sdk-example/testScripts** directory implements a complete usage scenario for our SDK. It showcases different transaction cases with no anomalies, demonstrates the retry mechanism in case of a timeout, and illustrates the backpressure mechanism in case of a high number of requests.
