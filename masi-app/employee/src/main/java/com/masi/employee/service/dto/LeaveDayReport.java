package com.masi.employee.service.dto;

import com.masi.employee.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDayReport {
    public record LeaveDayReportItem(Integer month, Float value) {

    }
    private String employeeId;
    private String fullName;
    private LocalDate joinDate;
    private LocalDate startDate;

    private double[] monthlyLeaves;

    private List<LeaveDayReportItem> leaveDayReportItems;
}
