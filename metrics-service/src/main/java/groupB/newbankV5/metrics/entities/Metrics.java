package groupB.newbankV5.metrics.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Metrics {

    Map<String, BigDecimal> values;

    LocalDateTime timestamp;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, BigDecimal> getValues() {
        return values;
    }

    public void setValues(Map<String, BigDecimal> values) {
        this.values = values;
    }

    public Metrics(Map<String, BigDecimal> values) {
        this.values = values;
    }

    public Metrics() {
        this.values = new HashMap<>();
    }

    public void addValue(String key, BigDecimal value) {
        this.values.put(key, value);
    }
}
