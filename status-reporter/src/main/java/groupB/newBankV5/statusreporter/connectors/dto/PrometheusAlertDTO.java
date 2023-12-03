package groupB.newBankV5.statusreporter.connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrometheusAlertDTO {
    private PrometheusDataDTO data;




    @JsonProperty("data")
    public PrometheusDataDTO getData() {
        return data;
    }

    public void setData(PrometheusDataDTO data) {
        this.data = data;
    }


}
