---
id: adrs-adr004
title: 'ADR004: Add Backend Service Healthcheck'
description: >
   Architecture Decision Record (ADR) to implement a healthcheck for backend services
---
## Context
Our Sdk clients need to know the status of the backend services to ensure the reliability of their applications. We need to provide a way to check the status of the backend services.

## Decision
We have decided to implement a new service in the backend that will call a prometheus endpoint to check the status of the backend services. this service will be called by the sdk clients to check the status of the backend services.
Since this service will be called by all the sdk clients, we will implement a cache of 5 seconds to avoid overloading the prometheus endpoint.
in addition to sending an up and down status, we will also send a "Partial Outage" status if the service is up but malfunctioning.


