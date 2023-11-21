package groupB.newbankV5.metrics.components;

import groupB.newbankV5.metrics.connectors.BusinessIntegratorProxy;
import groupB.newbankV5.metrics.connectors.PaymentGatewayProxy;

import groupB.newbankV5.metrics.controllers.dto.ApplicationDto;
import groupB.newbankV5.metrics.entities.*;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;
import groupB.newbankV5.metrics.exceptions.MerchantNotFoundException;
import groupB.newbankV5.metrics.interfaces.IMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class MetricsService implements IMetricsService {

    @Autowired
    private PaymentGatewayProxy paymentGatewayProxy;
    @Autowired
    private BusinessIntegratorProxy businessIntegratorProxy;
    @Override
    public long getConfirmedTransaction(String token) throws InvalidTokenException, ApplicationNotFoundException {
        ApplicationDto application = businessIntegratorProxy.validateToken(token);
        Long merchantId = application.getMerchant().getId();

        return this.paymentGatewayProxy.getConfirmedTransaction(merchantId);
    }
    @Override
    public long getAuthorizedTransaction(String token) throws InvalidTokenException, ApplicationNotFoundException {
        ApplicationDto application = businessIntegratorProxy.validateToken(token);
        Long merchantId = application.getMerchant().getId();
        return this.paymentGatewayProxy.getAuthorizedTransaction(merchantId);
    }


}