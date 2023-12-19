package groupB.newbankV5.metrics.controllers.dto;

import java.util.List;
import java.util.Map;

public class MetricRequest {

    private List<String> metrics;

    private Map<String, List<String>> filters;

    private TimeRange timeRange;

    private String period;
    private String resolution;

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }

    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<String>> filters) {
        this.filters = filters;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getPeriod(){
        return this.period;
    }
}
