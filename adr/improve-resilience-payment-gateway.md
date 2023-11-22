---
id: adrs-adr001
title: 'ADR001: Mitigation of Single Point of Failure for Payment Gateway'
description: >
   Architecture Decision Record (ADR) to Mitigate the Single Point of Failure (SPOF) in the payment gateway servive
---

## Context

The current payment gateway service operates as a Single Point of Failure (SPOF),
where all transactions are processed within a single service. This service manages
both payment authorization, communicating with the CCN service for card authorization
and the business integrator service for merchant information, and the confirmation
step, communicating with the payment service and external bank, and pushing to a Kafka topic.
This configuration poses potential risks to availability and resilience.

## Decision

The decision involves dividing the service into two separate services with clearly defined responsibilities:

1. **Authorizer Service:**
   - Responsible for the initial processing of transactions.
   - Communicates with the CCN service to authorize the card and the business integrator for merchant information.

2. **Confirmation Service:**
   - Responsible for triggering fund settlement after authorization.
   - Communicates with the payment service in the case of internal payment or the external bank in the case of an external transaction.
   - Utilizes a Kafka topic to push payment information.

This division into two services aims to reduce the risk of system failure in case of issues with one of the services. The separation of responsibilities also facilitates maintenance, scalability, and more granular issue resolution.

### Architecture Change using Strangler Fig Pattern:

The adoption of the Strangler Fig Pattern will guide the implementation of this architectural change. The Strangler Fig Pattern involves gradually replacing parts of an existing system with new, independently deployable services. In this context:

- **Strangler Fig Pattern Steps:**
  1. **Identify Incremental Steps:**
      - Identify components and functionalities within the existing payment gateway that can be incrementally replaced.
  2. **Build New Authorizer Service:**
      - Develop and deploy the new Authorizer Service alongside the existing payment gateway.
  3. **Gradual Migration:**
      - Gradually route authorization requests to the new Authorizer Service while still using the existing service for other functionalities.
  4. **Monitor and Test:**
      - Monitor the performance and reliability of the new service.
      - Conduct thorough testing to ensure compatibility and accuracy.
  5. **Repeat for Confirmation Service:**
      - Apply the same steps for introducing the Confirmation Service.

This phased approach ensures minimal disruptions, allows for continuous testing, and facilitates a smooth transition from the monolithic payment gateway to the split services for authorization and confirmation.

The diagrams illistrate the architecture before and after this change :
![Architecture Before](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/adr/before.png)

![Architecture After](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/adr/after.png)

## Consequences

### Advantages:
1. Reduction of SPOF.
2. Improved resilience and availability.
3. Ease of maintenance and scalability.

### Disadvantages:
1. Increased complexity in terms of coordination between services.

## Follow-up

Monitoring of this decision will be conducted through continuous performance monitoring, availability checks, and user feedback. Adjustments to the architecture may be made as necessary.
