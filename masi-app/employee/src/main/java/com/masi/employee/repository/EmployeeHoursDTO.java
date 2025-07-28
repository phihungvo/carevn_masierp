package com.masi.employee.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeHoursDTO {
    @Column("employee_id")
    private String employeeId;

    @Column("total_hours")
    private Float totalHours;
}
