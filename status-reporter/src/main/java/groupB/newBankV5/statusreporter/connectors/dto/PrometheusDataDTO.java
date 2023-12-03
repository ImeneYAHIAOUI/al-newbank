package groupB.newBankV5.statusreporter.connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PrometheusDataDTO {
    private List<PrometheusGroupDTO> groups;

    // Getters and setters

    @JsonProperty("groups")
    public List<PrometheusGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<PrometheusGroupDTO> groups) {
        this.groups = groups;
    }






}
