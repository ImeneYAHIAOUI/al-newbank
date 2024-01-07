---
id: adrs-adr008
title: "ADR008: Implementation of a master-slave database"
description: >
  Architecture Decision Record (ADR) to Implement a master-slave database for business integrator service.
---

*Status: [Final]*

## Context:
The existing database for the Business Integrator service relies on PostgreSQL for both read and write operations. 
As the service continuously integrates new applications and merchants, and considering the critical role of the database in token verification each request from payment gateway authorization and confirmation, concerns have arisen regarding scalability and performance.

## Decision:
To address these concerns specifically in the context of the Business Integrator service, we have decided to implement a Master-Slave PostgreSQL database, where the master db will be responible of integrating the new applications, merchants and corresponding tokens, and the slave db will be responsible of verifying the merchants and the validity of token.

#### Flow Diagram of using the PostgreSQL master-slave db : 

![master-slave](https://github.com/pns-si5-al-course/al-newbank-23-24-al-23-24-b-v5/blob/main/adr/images/master-slave.png)

## Consequences:

### Advantages:
1. **Scalability:** Introducing a read-only slave database facilitates horizontal scaling, efficiently managing growing read operations from new applications and merchants.
2. **Performance Optimization:** Distributing read operations across a single slave database alleviates the master database's load, enhancing overall system performance for both read and write operations.
3. **High Availability:** This ensures continuous system availability by enabling the promotion of the slave database to master in the event of a master database failure, crucial for uninterrupted Business Integrator service operations.

### Disadvantages:
1. **Token Verification Bottleneck:** While read operations are distributed across the single slave database, token verification for payment gateway processes still relies on the master database, posing a potential bottleneck, since there is a data replication delay.






