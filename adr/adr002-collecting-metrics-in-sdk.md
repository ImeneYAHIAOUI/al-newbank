---
id: adrs-adr002
title: 'ADR002: Collection and Sending of Merchant Payment Metrics to Prometheus in the SDK'
description: >
   Architecture Decision Record (ADR) to Collect and Send of Merchant Payment Metrics to Prometheus in the SDK
---

## Context

We need to collect metrics related to payments made by merchants using the SDK we provide. These metrics are crucial for monitoring system performance, 
identifying bottlenecks, and detecting potential errors. We also need a centralized way to store and visualize these metrics.

## Decision

We have decided to integrate a metrics collection feature into our payment SDK, which will send these metrics to a centralized Prometheus server. 
We  use Prometheus metric exposition format on a dedicated API endpoint within the SDK.

## Considered Alternatives
Custom Metric Storage: We considered building a custom solution for storing and analyzing payment metrics. However, it might not provide the robust features of Prometheus.

## Consequences
Pros:
Centralization of payment metrics for global analysis.
Ease of integration with Prometheus.
Ability to quickly detect and resolve payment-related issues.
Cons:
Additional complexity in the SDK for metrics collection and exposition.


## Additional Documentation
Prometheus Metric Exposition Documentation
