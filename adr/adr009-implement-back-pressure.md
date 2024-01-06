---
id: adrs-adr009
title: "ADR008: Implementation of a back pressure mechanism along with Rate Limiter and Enhanced Circuit Breaker"
description: >
  Architecture Decision Record (ADR) to Implement a back pressure mechanism along with Rate Limiter and Enhanced Circuit Breaker for Client Requests.
---

*Status: [Final]*

## Context:
The current system lacks a proactive mechanism to handle situations where the number of client requests exceeds the defined limit, leading to potential service disruptions and performance issues. 

To address this, it is proposed to implement a back pressure mechanism, coupled with a rate limiter and an improved circuit breaker, to efficiently manage client requests and prevent service overload.

## Decision: 
To implement a back pressure mechanism, a rate limiter will be introduced to monitor and control the number of incoming client requests. When the defined limit is reached, the system will respond with a status 429 (Too Many Requests) and a specific retry-after time. This will prompt clients to resend their requests after a certain waiting period.

Additionally, the circuit breaker will be enhanced to automatically open for the duration specified in the response when the rate limiter triggers the 429 status. This improvement prevents any incoming calls during the open state, ensuring a more stable and resilient system during periods of high demand. After the designated period, the circuit breaker will automatically close, allowing normal operations to resume.

This diagram illustrates the diiferent states of the circuit breaker in this case : 
![closed](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/images/circuit-breaker-close.png)
![open](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/images/circuit-breaker-open.png)

## Consequences:

### Advantages:
1. **Improved Service Stability:** The back pressure mechanism ensures that the system remains stable even during periods of high client request volume.
2. **Efficient Resource Utilization:** The rate limiter optimizes resource utilization by controlling the rate of incoming requests, preventing overload.
3. **Enhanced Circuit Breaker Resilience:** The improved circuit breaker automatically adjusts its open duration based on the response, preventing excessive calls during periods of high demand.

## Disadvantages:
1. **Potential Delay:** Clients may experience a delay in processing their requests due to the rate limiter and circuit breaker mechanisms.
