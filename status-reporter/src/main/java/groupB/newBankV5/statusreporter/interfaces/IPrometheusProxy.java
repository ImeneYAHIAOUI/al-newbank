package groupB.newBankV5.statusreporter.interfaces;

import groupB.newBankV5.statusreporter.connectors.dto.PrometheusAlertDTO;
import groupB.newBankV5.statusreporter.connectors.dto.PrometheusCPUUsageDTO;
import groupB.newBankV5.statusreporter.connectors.dto.PrometheusTargetsDto;

public interface IPrometheusProxy {
    PrometheusTargetsDto retrieveActiveTargets();
    PrometheusCPUUsageDTO retrieveCPUUsage(String applicationName);
    PrometheusAlertDTO retrieveAlerts();
}
