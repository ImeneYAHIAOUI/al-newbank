package groupB.newBankV5.statusreporter.connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PrometheusGroupDTO {

        private List<PrometheusRuleDTO> rules;

        // Getters and setters





        @JsonProperty("rules")
        public List<PrometheusRuleDTO> getRules() {
            return rules;
        }

        public void setRules(List<PrometheusRuleDTO> rules) {
            this.rules = rules;
        }




    }
