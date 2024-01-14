# Setting Up Grafana Dashboard for your web service Metrics

In this tutorial, we will guide you through the process of setting up a Grafana dashboard to visualize metrics from your app's REST endpoint. Follow these steps to get started.

## Prerequisites

1. **Grafana Installation**: Ensure that Grafana is installed on your system. You can download it from [Grafana's official website](https://grafana.com/get).
2. **Metrics and backend-status endpoints**: Ensure that your app has the following endpoints:
   - `/metrics`: This endpoint should return the metrics of your payment website after fetching them using the `getMetrics(metricsRequest)` interface of the Newbank SDK.
   - `/backend-status` : This endpoint should return the health status of the backend's microservices by fetching them using the `getBackendStatus()` interface of the NewBank SDK.

## Step 1: Install JSON API Data Source

1. Open your Grafana instance in a web browser.

2. Navigate to the gear icon (⚙️) in the left sidebar and select "Data Sources."

3. Click on the "Add your first data source" button.

4. Search for "JSON API" in the available data sources and select it.

5. Configure the JSON API data source with the necessary details such as URL of your web service, access, and authentication settings. Save the configuration.

## Step 2: Import Grafana Dashboard JSON

1. Download the two dashboard import files in the `Dashboards` repository.

2. In Grafana, go to the "+" menu in the left sidebar and select "Import."

3. Click on "Upload .json file" and choose the dashboard JSON file.

4. Review the settings and make any necessary adjustments, such as updating the data source to the one you created in the previous step. Click on "Import" to import the dashboard.

5. Repeat the previous step for the second dashboard JSON file.

## Step 3: View the Dashboard

1. Navigate to the Grafana home page and select the "Dashboards" option in the left sidebar.
2. Select the dashboard you want to view from the list of dashboards.
3. You should now be able to see the dashboard with the metrics of your payment website and the dashboard with the health status of the backend's microservices.
4. You can also customize the dashboards by adding new panels, changing the layout, modifying the queries, etc.

# Metric Queries

When you import the metrics dashboard, the panels are already configured to fetch the metrics of the last 6 ours from your web service's `/metrics` endpoint with an 'H' resolution. You can modify the json object in the bodies of the requests to change the time range and resolution of the metrics.

## Obligatory fields:
- `period or timeRange`:
   - `period`: Period of the metrics query. Starts with `L` (last) followed by a number and a one of the following values :
      - `MI`(minutes)
      - `H` (hours)
      - `D` (days),
      - `M` (months)
      - `Y` (years).

     For example, `L6H` means last 6 hours, `L1M` means last month, `L2Y` means last 2 years.

   - `timeRange`: a json sub-object containing the fields `from` and `to` which are the start and end dates of the metrics query. it accepts the following date formats:
      - Date and Time with Seconds (ISO-8601): `YYYY-MM-DDTHH:MM:SSZ` (2024-01-12T12:34:56)
      - Date and Time with fractional seconds (ISO-8601): `YYYY-MM-DDTHH:MM:SS.sssZ` (2024-01-12T12:34:56.789Z)
      - Date only (ISO-8601): `YYYY-MM-DD` (2024-01-12)
      - Date and Time with Offset (ISO-8601): `YYYY-MM-DDTHH:MM:SS+HH:MM` (2024-01-12T12:34:56+01:00)
   - `resolution`: Specifies the time interval between two consecutive data points. It can be one of the following values:
      - `5M` (5 minutes)
      - `15M` (15 minutes)
      - `30M` (30 minutes)
      - `H` (1 hour)
      - `D` (1 day)
      - `W` (1 week)
      - `M` (1 month)
      - `Y` (1 year).

### Example of a valid minimalistic metrics query with the period field:
```json
{
    "period": "L6H",
    "resolution": "5M"
}
```

### Example of a valid minimalistic metrics query with the timeRange field:
```json
{
    "timeRange": {
        "from": "2024-01-12T12:34:56+01:00",
        "to": "2024-01-12T17:34:56+01:00"
    },
    "resolution": "5M"
}
```

By default, the metrics query returns the following metrics:

- `transactionCount`: The number of transactions.
- `TransactionSuccessRate`: The percentage of successful transactions.
- `TransactionFailureRate`: The percentage of failed transactions.
- `totalAmountSpent`: The total amount spent.
- `averageAmountSpent`: The average amount spent per transaction.
- `totalFees`: The total fees paid.
- `averageFees`: The average fees paid per transaction.
- `totalRequestsCount`: The total number of requests sent from the web service to the payment gateway.
- `successfulRequestsCount`: The number of successful requests sent from the web service to the payment gateway.
- `failedRequestsCount`: The number of failed requests sent from the web service to the payment gateway.
-  `successfulRequestsRate` : The percentage of successful requests sent from the web service to the payment gateway.
- `failedRequestsRate` : The percentage of failed requests sent from the web service to the payment gateway.
- `averageRequestTime`: The average time taken by the payment gateway to process a request.

## Optional fields:

- `metrics`: A list of metrics to be returned. It can be one or more of the metrics listed above. If not specified, all metrics are returned.
- `filters` : A set of filters to be applied to the metrics query, each filter has a key (filter name) and a list of values. The following filters are available:
   - `status`: By passing the filter, only the metrics concerning the transactions with the specified status are returned. The possible values are:
      - `AUTHORISED`
      - `CONFIRMED`
      - `FAILED`
      - `PENDING_SETTLEMENT`
      - `SETTLED`
   - `creditCardType`: By passing the filter, only the metrics concerning the transactions with the specified credit card type are returned. This filter takes only one value. If multiple values are passed, only the first one is considered. The possible values are:
      - `credit`
      - `debit`

### Example of a valid metrics query with all the fields:
```json
{
    "period": "L6H",
    "resolution": "5M",
    "metrics": [
        "transactionCount",
        "TransactionSuccessRate",
        "TransactionFailureRate",
        "totalAmountSpent"
    ],
    "filters": {
        "status": [
            "AUTHORISED",
            "CONFIRMED"
        ],
        "creditCardType": [
            "credit"
        ]
    }
}
```