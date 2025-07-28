package com.masi.sale.service.cleanups;

import com.masi.sale.service.ContractService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class ContractCleanupScheduler {

    private final ContractService contractService;

    public ContractCleanupScheduler(ContractService contractService) {
        this.contractService = contractService;
    }

    // Chạy lúc 12h đêm hàng ngày
    //@Scheduled(cron = "0 0 0 * * *")
    public void cleanupOldDeletedContracts() {
        ZonedDateTime thirtyDaysAgo = ZonedDateTime.now().minusDays(30);
        contractService.cleanupOldDeletedContracts(thirtyDaysAgo)
            .subscribe();
    }
}
