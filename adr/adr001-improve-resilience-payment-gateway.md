---
id: adrs-adr001
title: 'ADR001: Mitigation of Single Point of Failure for Payment Gateway'
description: >
   Architecture Decision Record (ADR) to Mitigate the Single Point of Failure (SPOF) in the payment gateway servive
---

## Context

The current payment gateway service operates as a Single Point of Failure (SPOF),
where all transactions are processed within a single service. This service manages
both payment authorization and the confirmation step.
This configuration poses potential risks to availability and resilience.

## Decision

The decision involves dividing the service into two separate services with clearly defined responsibilities:

1. **Authorizer Service:**
   - Responsible for the initial authorization of transactions.

2. **Confirmation Service:**
   - Responsible for confirming transaction after authorization.

This division into two services aims to reduce the risk of system failure in case of issues with one of the services. The separation of responsibilities also facilitates maintenance, scalability, and more granular issue resolution.

### Architecture Change using Strangler Fig Pattern:

The adoption of the Strangler Fig Pattern will guide the implementation of this architectural change which involves gradually replacing parts of the existing payment gateway with the new services. In this context:
- **Strangler Fig Pattern Steps:**
  1. **Identify Incremental Steps:**
      - Identify components and functionalities within the existing payment gateway that can be incrementally replaced.
  2. **Build New Authorizer Service:**
      - Develop, build and run the new Authorizer Service alongside the existing payment gateway.
  3. **Gradual Migration:**
      - Gradually route authorization requests to the new Authorizer Service while still using the existing service for other functionalities.
  4. **Monitor and Test:**
      - Monitor the performance and reliability of the new service.
      - Conduct thorough testing to ensure compatibility and accuracy.
  5. **Repeat for Confirmation Service:**
      - Apply the same steps for introducing the Confirmation Service.

This phased approach ensures minimal disruptions, allows for continuous testing, and facilitates a smooth transition from the monolithic payment gateway to the split services for authorization and confirmation.

### Payment Gateway Architecture: Before and After Splitting :
Before :
![Architecture Before](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/images/before.png)
After :
![Architecture After](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/images/after.png)

## Consequences

### Advantages:
1. Reduction of SPOF.
2. Improved resilience and availability.
3. Ease of maintenance and scalability.

### Disadvantages:
1. Increased complexity in terms of coordination between services.
2. Both services communicate with the same database, which may affect database performance and introduce a bottleneck in database transactions.

## Follow-up

Monitoring of this decision will be conducted through continuous performance monitoring, availability checks, and user feedback. Adjustments to the architecture may be made as necessary.
