package groupB.newBankV5.statusreporter.components;

import groupB.newBankV5.statusreporter.connectors.dto.ActiveTargetDto;
import groupB.newBankV5.statusreporter.connectors.dto.ActiveTargetsDto;
import groupB.newBankV5.statusreporter.connectors.dto.PrometheusRuleDTO;
import groupB.newBankV5.statusreporter.connectors.dto.PrometheusTargetsDto;
import groupB.newBankV5.statusreporter.entities.ServiceStatus;
import groupB.newBankV5.statusreporter.interfaces.IPrometheusProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Component
public class ServiceStatusRetriever implements IServiceStatusRetriever {

    private static final Logger log = LoggerFactory.getLogger(ServiceStatusRetriever.class);

    private IPrometheusProxy prometheusProxy;

    @Autowired
    public ServiceStatusRetriever(IPrometheusProxy prometheusProxy) {
        this.prometheusProxy = prometheusProxy;
    }

    @Override
    @Cacheable(value = "statusReport", key = "'statusReport'", cacheManager = "cacheManager")
    public List<ServiceStatus> retrieveServiceStatus() {

        List< PrometheusRuleDTO> rules = prometheusProxy.retrieveAlerts().getData().getGroups().get(0).getRules();
        List<ActiveTargetDto> targets = prometheusProxy.retrieveActiveTargets().getData().getActiveTargets().stream()
                .filter(target -> target.getLabels().getApplication() != null).toList();
        Map<ActiveTargetDto, List<PrometheusRuleDTO>> targetRulesMap = targets.stream()
                .collect(Collectors.toMap(target -> target, target -> rules.stream()
                        .filter(rule -> rule.getLabels().getJob().equals(target.getLabels().getJob()))
                        .toList()));

        List<ServiceStatus> ServiceStatuses = targetRulesMap.entrySet().stream()
                .map(entry -> {
                    String health = Objects.equals(entry.getKey().getHealth(), "down") ? "down" : entry.getValue().stream()
                            .anyMatch(rule -> Objects.equals(rule.getState(), "firing")) ? "degraded" : "up";
                    return new ServiceStatus(entry.getKey().getLabels().getApplication(), health);
                }).toList();

        return ServiceStatuses;

    }

}
