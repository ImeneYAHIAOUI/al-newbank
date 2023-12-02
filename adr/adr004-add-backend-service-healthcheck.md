---
id: adrs-adr004
title: 'ADR004: Add Backend Service Healthcheck'
description: >
   Architecture Decision Record (ADR) to implement a healthcheck for backend services
---
## Context
Our Sdk clients need to know the status of the backend services to ensure the reliability of their applications. We need to provide a way to check the status of the backend services.

## Decision
To address this need, we have decided to implement a new service within the backend infrastructure. This service will be responsible for querying a Prometheus endpoint to determine the status of backend services.

Given the potential high frequency of requests from various SDK clients, we implented have opted to implemet a cache-aside strategy with a duration of 5 seconds to minimize the load on the Prometheus endpoint.

Additionally, the healthcheck service will send not only the  "Up" or "Down" status but also  a "Partial Outage" status if a service is up but malfunctioning.


