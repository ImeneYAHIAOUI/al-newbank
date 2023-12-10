* What was done :
  - For business metrics, we  chose the first solution—implementing an event-sourcing based Change Data Capture (CDC) pattern.
  - A Basic circuit breaker is implemented in the SDK side to only send requests to the backend system  when the system is in an open state.
  - A rate limiter was added in the load balancer behind which are our backend service.

* What is planned :
  - The SDK receives service statuses in JSON format. For next week, we are planning to deliver a client solution that enables them to display the statuses through a dashboard, utilizing platforms such as Grafana or others.
  - Implementing a Timeout Pattern to prevent indefinite blocking in case of issues.
  - Add a clear documentation with compelling usecase driven scenarios to showcase examples of using our SDK to the merchants.


* Issues : No issues this week.

* Risks :  We need to be cautious not to exhaust all our credits on cloud.

* RYG flag : green
