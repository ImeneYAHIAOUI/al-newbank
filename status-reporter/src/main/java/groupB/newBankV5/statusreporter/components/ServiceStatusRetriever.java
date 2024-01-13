package groupB.newBankV5.statusreporter.components;

import groupB.newBankV5.statusreporter.connectors.dto.ActiveTargetDto;
import groupB.newBankV5.statusreporter.connectors.dto.PrometheusRuleDTO;
import groupB.newBankV5.statusreporter.controllers.StatusController;
import groupB.newBankV5.statusreporter.entities.ServiceStatus;
import groupB.newBankV5.statusreporter.entities.ServiceStatusWithMetrics;
import groupB.newBankV5.statusreporter.exceptions.ApplicationNotFoundException;
import groupB.newBankV5.statusreporter.exceptions.InvalidTokenException;
import groupB.newBankV5.statusreporter.interfaces.IBusinessIntegrator;
import groupB.newBankV5.statusreporter.interfaces.IPrometheusProxy;
import groupB.newBankV5.statusreporter.interfaces.IServiceStatusRetriever;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public List<ServiceStatusWithMetrics> retrieveServiceStatus(String token) throws InvalidTokenException, ApplicationNotFoundException {
        businessIntegratorProxy.validateToken(token);
        return retrieveStatusFromPrometheus();
    }


    @Override

    public List<ServiceStatusWithMetrics> retrieveStatusFromPrometheus() {
        List< PrometheusRuleDTO> rules = prometheusProxy.retrieveAlerts().getData().getGroups().get(0).getRules();
        List<ActiveTargetDto> targets = prometheusProxy.retrieveActiveTargets().getData().getActiveTargets().stream()
                .filter(target -> target.getLabels().getApplication() != null).toList();
        Map<ActiveTargetDto, List<PrometheusRuleDTO>> targetRulesMap = targets.stream()
                .collect(Collectors.toMap(target -> target, target -> rules.stream()
                        .filter(rule -> rule.getLabels().getJob().equals(target.getLabels().getJob()))
                        .toList()));

        List<ServiceStatus> ServiceStatuses = targetRulesMap.entrySet().stream()
                .map(entry -> {
                    int health = Objects.equals(entry.getKey().getHealth(), "down") ? 2 : entry.getValue().stream()
                            .anyMatch(rule -> Objects.equals(rule.getState(), "firing")) ? 3 : 1;
                    return new ServiceStatus(entry.getKey().getLabels().getApplication(), health);
                }).toList();


        return ServiceStatuses.stream()
                .map(serviceStatus -> {
                    double cpuUsage = getServiceNameCPUStatus(serviceStatus.getServiceName());
                    log.info("CPU usage for service " + serviceStatus.getServiceName() + " is " + cpuUsage);

                    int waitingTime = cpuUsage > 0.45 ? 2 * (int) ((cpuUsage * 100) - 45) : 0;
                    log.info("Waiting time for service " + serviceStatus.getServiceName() + " is " + waitingTime);
                    if (waitingTime > 0) {
                        serviceStatus.setServiceStatus(3);
                    }
                    return new ServiceStatusWithMetrics(serviceStatus.getServiceName(), serviceStatus.getServiceStatus(), waitingTime );
                }).toList();
    }

    private Double getServiceNameCPUStatus(String serviceName) {
        return Double.parseDouble(
                prometheusProxy.retrieveCPUUsage(serviceName).getData().getResult().get(0).getValue().get(1));
    }


    public ServiceStatusWithMetrics checkServiceAvailability(String serviceName){
        if(!StatusController.ACTIVATE_CPU){
            return new ServiceStatusWithMetrics(serviceName, 1, 0);
        }
        Optional<ServiceStatusWithMetrics> serviceStatusWithMetrics =
        this.retrieveStatusFromPrometheus().stream()
                .filter(serviceStatus -> serviceStatus.getServiceName().contains(serviceName))
                .min(Comparator.comparingInt(ServiceStatusWithMetrics::getWaitingTime)
                        .thenComparingInt(ServiceStatusWithMetrics::getServiceStatus));
        return serviceStatusWithMetrics.orElseGet(() -> new ServiceStatusWithMetrics(serviceName, 1, 0));
        }

}
