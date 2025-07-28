package com.masi.employee.service.reports;

import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class HumanResourceChangeReport {
    @Column("total_leave")
    private int totalLeave;
    @Column("total_join")
    private int totalJoin;
    @Column("month")
    private String month;
    private boolean isSum = false;

    @Data
    @ParameterObject
    public static class Query {
        private LocalDate fromDate = LocalDate.of(2021, 1, 1);
        private LocalDate toDate = LocalDate.of(2099, 1, 1);
        private String company;
        private Pageable pageable;
        private List<UUID> departmentIds;
    }
}
