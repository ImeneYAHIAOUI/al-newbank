# Setting Up Grafana Dashboard for Your web service Metrics

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