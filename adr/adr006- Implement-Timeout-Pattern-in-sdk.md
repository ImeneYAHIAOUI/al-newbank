---
id: adrs-adr006
title: "ADR006: Implementation of Timeout Pattern"
description: >
  Architecture Decision Record (ADR) to implement of Timeout Pattern
---

## Context:

Within our backend architecture, the payment authorization and confirmation process introduces a critical dependency chain. For example, the Payment Gateway authorizer service orchestrates the authorization by interfacing with the Credit Card Network (CCN) service, which, in turn, communicates with external bank system. This intricate dependency structure poses a potential risk of network performance issues, leading to prolonged waits and indefinite blocking during service calls. Such challenges can significantly impact the overall responsiveness and reliability of the payment authorization process.
This diagram illustrates the dependency chain of Payment Gateway Authorizer : 
![Timeout](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/images/timeout.png)

## Decision:

To proactively address the risk associated with potential network performance issues and indefinite blocking during service calls, we propose the implementation of the Timeout Pattern. This strategic pattern involves setting time limits for both Payment Gateway Authorizer and confirmation, and CCN service microservices. Also, to ensure comprehensive coverage, the Timeout Pattern will be integrated into the SDK, enabling clients to benefit from timely responses and preventing indefinite blocking at the client level.

## Consequences:

* Improved system responsiveness by avoiding prolonged waits for unresponsive services.
* Mitigation of potential performance bottlenecks due to indefinite blocking during service calls.
* Enhacing Resilience against fluctuations in network conditions or temporary service unavailability.
  
## Implementation Plan

1. Timeout Configurations: Set timeout limits for both Payment Gateway services and CCN service based on response times, network latency, and acceptable system responsiveness.
2. SDK Integration: Add Timeout Pattern to the SDK. Developers using our SDK can pick their own timeout limit for the functions they use, preventing indefinite blocking.

## Risks and Considerations
While the Timeout Pattern offers significant advantages, it is essential to consider potential risks and challenges:
1. **Configurability:** Ensuring that the timeout values are configurable and adjustable based on different service requirements and environmental conditions.
