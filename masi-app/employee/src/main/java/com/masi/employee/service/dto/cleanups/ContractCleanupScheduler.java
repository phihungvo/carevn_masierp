package com.masi.employee.service.dto.cleanups;

import com.masi.employee.repository.AnnualLeaveRepository;
import com.masi.employee.service.AnnualLeaveService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
@Component
public class ContractCleanupScheduler {

    private final AnnualLeaveService annualLeaveService;

    public ContractCleanupScheduler(AnnualLeaveService annualLeaveService) {
        this.annualLeaveService = annualLeaveService;
    }


    // Chạy vào 0 giờ ngày 1 mỗi tháng
//    @Scheduled(cron = "0 0 0 1 * *")
    public void cleanupOldDeletedContracts() {
        int currentMonth = ZonedDateTime.now().getMonthValue();
        int currentYear = ZonedDateTime.now().getYear();
        int startOfMonth = currentYear * 10000 + currentMonth * 100 + 1;

        annualLeaveService.resetDayOffEmployee(startOfMonth)
            .subscribe();
    }


}
