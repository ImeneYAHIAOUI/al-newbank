---
id: adrs-adr002-bis
title: 'ADR002-bis: Collection and Sending of Merchant Payment Metrics to Prometheus in the SDK'
description: >
   Architecture Decision Record (ADR) to Collect and Send of Merchant Payment Metrics to Prometheus in the SDK
---
*Status: [Final]*

## Background

Previously, in order to handle technical metrics collection related to the payments requests, we resorted to hosting a client side prometheus server in our SDK, which is in charge of pushing the technical metrics to the central prometheus thus exposing this latter to the outside world and making it face a big threat of overload, denial of serice or other attacks.

Additionally, upon SDK failure, all aggregated metrics are reset, which leads to our server-side metrics being inconsitent and incomplete.

## Context

We need to address the shortcomings of the previously implemented solution and revisit our way to push the metrics to the backend from the SDK.

## Decision

We decided to change the way we push the technical metrics to the backend from our SDK. The SDK now will resort to REST calls on the Metrics Service to post technical metrics on each single request made. 

Instead of aggregating them in a local prometheus client in SDK using prometheus primitive types, the metrics will be simple jsons stored in the metrics database.

Additionally, the metrics service will expose an endpoint to access these metrics allowing to create alerts and visualization dashboards based on these metrics.

## Consequences
**Pros:**

* Scalable solution : The addressed metrics service is stateless and can be scaled horizontally.
* Disallowed external access to Prometheus : The chosen solution ensures that our interal server is safeguarded against unauthorized access and potential breaches.

**Cons:**

* Increased Application Complexity: While the solution effectively secures Prometheus, it introduces additional layers of complexity to our application code in order to provide consumable metrics endpoint for visualization tools such as Grafana : period, resolution and filter support for metrics data points retrieval.
* New Critical Entry point : The metrics service being used to hydrate dashboard denotes a high frequency of calls, making it a new brittle component

