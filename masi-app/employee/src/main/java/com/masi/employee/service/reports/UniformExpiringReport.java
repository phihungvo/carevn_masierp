package com.masi.employee.service.reports;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UniformExpiringReport {

    public enum Type {
        UNALLOCATED, ALLOCATED, BOTH
    }

    @Column(name = "employee_id")
    private java.util.UUID employeeId;
    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "full_name")
    private String fullName;

//    @Column(name = "type")
    private String type="";
    // employee_profile.start_work_date
    @Column(name = "start_work_date")
    private java.time.LocalDate startWorkDate;

    @JsonProperty("expired_date")
    public java.time.LocalDate getExpiredDate() {
        int currentYear = LocalDate.now().getYear();
        if (startWorkDate == null) {
            return LocalDate.of(currentYear, 1, 1);
        }
        return LocalDate.of(currentYear, startWorkDate.getMonth(), startWorkDate.getDayOfMonth());
    }
}
