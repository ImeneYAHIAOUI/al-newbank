---
id: adrs-adr007
title: "ADR007: Implementation of a Circuit Breaker in the SDK"
description: >
  Architecture Decision Record (ADR) to Implement a circuit breaker on the client side (SDK)
---

*Status: [Final]*

## Context:

The current SDK lacks a proactive mechanism to assess the health of backend services before initiating requests. There is a need to introduce a mechanism that prevents unnecessary service calls during disruptions.

## Decision:

To enhance the resilience of the system and benefit from the status checks established previosuly in the status reporter service, a decision has been made to introduce a circuit breaker mechanism, in which the SDK will check the availability of backend services before each request, ensuring that it only interacts with services that are in a healthy state.

In case of unavailability, the SDK will directly inform the client that the service is not accessible.

### Flow Diagram of SDK Reaction Through Circuit Breaker in Case of Service Availability and Unavailability :

![availibity](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/adr/images/circuit-breaker-availability.png)

## Consequences
### Advantages:
1. **Enhanced Resilience:** The system becomes more resilient by having the SDK avoid unnecessary service requests during disruptions.
2.  **Effective Use of Health Checks:** The circuit breaker efficiently leverages the existing health check done to determine the availability of backend services.

### Disadvantages:
1. **Potential Delay:** The circuit breaker mechanism will introduce a slight delay in making a service request.

