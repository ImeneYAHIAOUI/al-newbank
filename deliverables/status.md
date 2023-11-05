* What was done :
  - Membership within our system is no longer managed in PaymentGateway; it is now handled by a separate microservice  "Business Integrator. 

  - We identified a risk in our payment system related to idempotence, which ensures safe repetition of actions. To fix this, we implemented an exchange protocol that guarantees unique processing of each transaction, even in case of client-side failure. This minimizes the risk of billing errors or duplicates.
  
  - We have enhanced fees management. The charges for a transaction are variable and contingent on the card type(debit or credit), our gateway margin, and the merchant's banking margin.
* What is planned :
    - To protect the gateway, implement a rate limiter. This will help regulate the number of requests that can be made to the gateway within a specified time frame
    
    - simulate a large number of payment requests to evaluate how the system handles high traffic scenarios.
* Issues : No issues for this week
* Risk :
* RYG flag : green 
