package com.masi.logistics.service.cron;

import com.masi.logistics.service.SupplierContractService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@AllArgsConstructor
public class SupplierContract {
    private final SupplierContractService supplierContractService;
    private static final Logger LOG = LoggerFactory.getLogger(SupplierContractService.class);

    //@Scheduled(fixedRate = 1000 * 60 * 5)
    public void updateExpiredContract() {
        LOG.info("Start update expired contracts at {}", ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        supplierContractService.updateExpiredContracts().subscribe();
        LOG.info("End update expired contracts at {}", ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
    }
}
