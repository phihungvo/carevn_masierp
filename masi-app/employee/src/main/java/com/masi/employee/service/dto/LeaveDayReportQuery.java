package com.masi.employee.service.dto;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LeaveDayReportQuery {

    @Column("employee_id")
    private String employeeId;
    @Column("full_name")
    private String fullName;

    @Column("join_date")
    private LocalDate joinDate;

    @Column("start_date")
    private LocalDate startDate;
    @Column("month")
    private Integer month;
    @Column("value")
    private Float value;
}
