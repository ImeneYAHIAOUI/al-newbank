package groupB.newBankV5.statusreporter.components;

import groupB.newBankV5.statusreporter.entities.ServiceStatus;
import groupB.newBankV5.statusreporter.interfaces.IPrometheusProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public List<ServiceStatus> retrieveServiceStatus() {
        return prometheusProxy.retrieveActiveTargets().getData().getActiveTargets().stream()
                .filter(target -> target.getLabels().getApplication() != null)
                .map(target -> new ServiceStatus(target.getLabels().getApplication(), target.getHealth()))
                .toList();

    }

}
