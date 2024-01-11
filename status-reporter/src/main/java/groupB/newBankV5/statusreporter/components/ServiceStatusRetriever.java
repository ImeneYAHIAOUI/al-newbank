package groupB.newBankV5.statusreporter.components;

import groupB.newBankV5.statusreporter.connectors.dto.ActiveTargetDto;
import groupB.newBankV5.statusreporter.connectors.dto.PrometheusRuleDTO;
import groupB.newBankV5.statusreporter.entities.ServiceStatus;
import groupB.newBankV5.statusreporter.exceptions.ApplicationNotFoundException;
import groupB.newBankV5.statusreporter.exceptions.InvalidTokenException;
import groupB.newBankV5.statusreporter.interfaces.IBusinessIntegrator;
import groupB.newBankV5.statusreporter.interfaces.IPrometheusProxy;
import groupB.newBankV5.statusreporter.interfaces.IServiceStatusRetriever;
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
    private final IBusinessIntegrator businessIntegratorProxy;
    private final IPrometheusProxy prometheusProxy;

    @Autowired
    public ServiceStatusRetriever(IBusinessIntegrator businessIntegratorProxy, IPrometheusProxy prometheusProxy) {
        this.businessIntegratorProxy = businessIntegratorProxy;
        this.prometheusProxy = prometheusProxy;
    }

    @Override
    public List<ServiceStatus> retrieveServiceStatus(String token) throws InvalidTokenException, ApplicationNotFoundException {
        businessIntegratorProxy.validateToken(token);
        return retrieveStatusFromPrometheus();
    }


    @Override
    public List<ServiceStatus> retrieveStatusFromPrometheus() {
        List< PrometheusRuleDTO> rules = prometheusProxy.retrieveAlerts().getData().getGroups().get(0).getRules();
        List<ActiveTargetDto> targets = prometheusProxy.retrieveActiveTargets().getData().getActiveTargets().stream()
                .filter(target -> target.getLabels().getApplication() != null).toList();
        Map<ActiveTargetDto, List<PrometheusRuleDTO>> targetRulesMap = targets.stream()
                .collect(Collectors.toMap(target -> target, target -> rules.stream()
                        .filter(rule -> rule.getLabels().getJob().equals(target.getLabels().getJob()))
                        .toList()));

        return targetRulesMap.entrySet().stream()
                .map(entry -> {
                    int health = Objects.equals(entry.getKey().getHealth(), "down") ? 2 : entry.getValue().stream()
                            .anyMatch(rule -> Objects.equals(rule.getState(), "firing")) ? 3 : 1;
                    return new ServiceStatus(entry.getKey().getLabels().getApplication(), health);
                }).toList();
    }

    public boolean checkServiceAvailability(String serviceName){
            return this.retrieveStatusFromPrometheus().stream()
                    .filter(serviceStatus -> serviceStatus.getServiceName().contains(serviceName))
                    .allMatch(serviceStatus -> serviceStatus.getServiceStatus() == 1);
        }

}
