package com.masi.employee.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeHaveLeaveRequestToDate {
    private UUID employeeId;
    private Collection<UUID> shiftIds;
}
