---
id: adrs-adr006
title: "ADR006: Implementation of Timeout Pattern"
description: >
  Architecture Decision Record (ADR) to implement of Timeout Pattern
---

*Status: [Open]*

## Context:

There is a need to prevent indefinite blocking during service calls and enhance the overall responsiveness of the system.

## Decision:

Implement the Timeout Pattern to set time limits for interactions with backend services, ensuring timely responses and preventing indefinite blocking.

## Consequences:

* Improved system responsiveness by avoiding prolonged waits for unresponsive services.
* Mitigation of potential performance bottlenecks due to indefinite blocking during service calls.
  
## Architectural solutions proposed

1. **Promises for Asynchronous Operations:** Ensure that all asynchronous operations, such as service calls, are wrapped in promises to facilitate the implementation of the Timeout Pattern.

2. **Timeout Mechanism:** Develop a reusable timeout function or module that can be easily applied to different asynchronous operations across the codebase.

3. **Timeout Configuration:**
    - **Service-Specific Configurations:** Explore the option of allowing configurable timeouts for each service individually. This would involve maintaining a configuration file to associate specific services with their respective timeout values.
    - **Global Timeout:** Alternatively, consider having a global, fixed timeout for all services if uniform response times are acceptable.

## Risks and Considerations
While the Timeout Pattern offers significant advantages, it is essential to consider potential risks and challenges:

1. **Configurability:** Ensuring that the timeout values are configurable and adjustable based on different service requirements and environmental conditions.

