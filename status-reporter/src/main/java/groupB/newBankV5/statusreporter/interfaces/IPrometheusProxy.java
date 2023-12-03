package groupB.newBankV5.statusreporter.interfaces;

import groupB.newBankV5.statusreporter.connectors.dto.PrometheusAlertDTO;
import groupB.newBankV5.statusreporter.connectors.dto.PrometheusTargetsDto;

public interface IPrometheusProxy {
    PrometheusTargetsDto retrieveActiveTargets();


    PrometheusAlertDTO retrieveAlerts();
}
