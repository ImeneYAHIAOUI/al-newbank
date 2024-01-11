package groupB.newBankV5.statusreporter.connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PrometheusCPUUsageDTO {
    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @JsonProperty("resultType")
        private String resultType;

        @JsonProperty("result")
        private List<Result> result;

        public String getResultType() {
            return resultType;
        }

        public void setResultType(String resultType) {
            this.resultType = resultType;
        }

        public List<Result> getResult() {
            return result;
        }

        public void setResult(List<Result> result) {
            this.result = result;
        }
    }

    public static class Result {
        @JsonProperty("metric")
        private Metric metric;

        @JsonProperty("value")
        private List<String> value;

        public Metric getMetric() {
            return metric;
        }

        public void setMetric(Metric metric) {
            this.metric = metric;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }

    public static class Metric {
        @JsonProperty("__name__")
        private String name;

        @JsonProperty("application")
        private String application;

        @JsonProperty("instance")
        private String instance;

        @JsonProperty("job")
        private String job;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public String getInstance() {
            return instance;
        }

        public void setInstance(String instance) {
            this.instance = instance;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }
    }
}
