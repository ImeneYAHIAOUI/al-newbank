* What was done :
  - A new service was created to provide the status of the backend services based on prometheus. The API to interact with it was added to the sdk. A prometheus alert manager was also put in place for internal alerts.
  - We revisited the metric sending strategy and we are deciding on wether to implement an event-sourcing based CDC pattern or to create CronJob Services to mimic an async CDC pattern.
  - Respective ADRS were created.

* What is planned :
  - Implement one of the two solutions for the sending metrics.
  - Implement back pressure to handle peak times from the SDK side.
  - Revisit the resilience aspects of our architecture  

* Issues : We are facing difficulties configuring the kafka connectors for a POC of the CDC pattern.
* Risks : Delay on the non functional aspect of resilience as client-side backpressure is not put in place yet

* RYG flag : green
