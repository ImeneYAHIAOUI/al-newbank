---
id: adrs-adr007
title: "ADR006: Implementation of a circuit breaker in sdk"
description: >
  Architecture Decision Record (ADR) to implement a circuit breaker
---

## Context:

The current SDK lacks a proactive mechanism to assess the health of backend services before initiating requests. There is a need to introduce a mechanism that prevents 
unnecessary service calls during disruptions.

## Decision:

To enhance the resilience of the system and benefit from the health checks established in the status reporter service, a decision has been made to introduce a circuit breaker,
 which will be designed to check the availability of backend services before each request, ensuring that the SDK only interacts with services that are in a healthy state. 
In cases of unavailability, the SDK will directly inform the client that the service is not accessible.

## Consequences:

### Advantages:
1. **Enhanced Resilience:** The SDK becomes more resilient by avoiding unnecessary requests during backend service disruptions.
2.  **Effective Use of Health Checks:** The circuit breaker efficiently leverages the existing health check done to determine the availability of backend services.

### Disadvantages:
1. **Potential Delay:** The circuit breaker mechanism will introduce a slight delay in making backend service requests.
