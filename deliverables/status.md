* What was done :
  - We addressed the Single Point of Failure (SPOF) in our payment gateway by dividing it into two separate services.
    
  - A retry mechanism was incorporated into the SDK, utilizing an exponential backoff strategy, and retry settings were made available to merchant developers.
  
  - A MVP feature to track payment metrics (authorized,confirmed,failed transactions) for the merchant was implemented.

* What is planned :
    - Integrating a backpressure mechanism with a rate limiter into the SDK to strengthen the resilience of the gateway services.
 
    - Exploring enhancements to the current metric sending method, including batch transmission, and assessing the potential integration of a database or cache system within our SDK.
 
    - Improving error handling in the SDK.
 
* Issues : The exact requirements for implementing service status are not fully comprehended.
* Risks : Uncertainty exists regarding whether the implemented features align with client needs due to unclear specifications. Consideration of whether additional methods, such as fees, should be added to the SDK.
* RYG flag : yellow 
