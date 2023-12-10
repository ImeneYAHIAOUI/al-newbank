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
