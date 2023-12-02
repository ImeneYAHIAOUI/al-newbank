package groupB.newBankV5.statusreporter.connectors;

import groupB.newBankV5.statusreporter.connectors.dto.PrometheusTargetsDto;
import groupB.newBankV5.statusreporter.interfaces.IPrometheusProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Component
public class PrometheusProxy implements IPrometheusProxy {

    private static final Logger log = LoggerFactory.getLogger(PrometheusProxy.class.getName());

    @Value("${prometheus.host.baseurl:}")
    private String prometheusHostandPort;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Cacheable(value = "activeTargets", key = "'activeTargets'", cacheManager = "cacheManager")
    public PrometheusTargetsDto retrieveActiveTargets() {
        log.info("Sending request to prometheus to retrieve active targets");
        return restTemplate.getForEntity(
                prometheusHostandPort + "/api/v1/targets",
                PrometheusTargetsDto.class).getBody();

    }


}
