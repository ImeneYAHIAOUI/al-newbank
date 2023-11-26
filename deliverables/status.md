* What was done :
  - We addressed the Single Point of Failure (SPOF) in our payment gateway by dividing it into two separate services (one for authorization and one for confirmation).
    
  - A retry mechanism was incorporated into the SDK, utilizing an exponential backoff strategy, and retry settings were made available to merchant developers.
  
  - A MVP feature to track payment metrics (authorized,confirmed,failed transactions) for the merchant was implemented.
 
  - Respective adrs were created.

* What is planned :
    - Integrating a backpressure mechanism into the SDK to strengthen the resilience of the gateway services and inform the clients using the sdk. 
 
    - Exploring (functional and architectural) enhancements to the current metric sending strategy, and assessing the potential integration of an applicative cache within the SDK.
 
    - Improving error handling in the SDK by giving more readable output to the client (potentially through a RAML format).
 
* Issues : The exact requirements for implementing service status are not fully comprehended : performing health checks before calling the api's ? exposing a status interface in the sdk ?

* Risks : Consequently, due to the issues faced, uncertainty exists regarding whether the implemented features align **excatly** with the described needs overhall, which would require further enhancement and changes to adjust to the functional needs

* RYG flag : green
