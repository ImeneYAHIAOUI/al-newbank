package groupB.newBankV5.statusreporter.connectors.dto;

import java.util.List;

public class PrometheusRuleDTO {

    private String state;
    private LabelsDto labels;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LabelsDto getLabels() {
        return labels;
    }

    public void setLabels(LabelsDto labels) {
        this.labels = labels;
    }
}
