package com.masi.employee.service.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masi.employee.service.dto.UniformDTO;
import com.masi.employee.service.dto.UniformOrderDTO;
import com.masi.employee.service.dto.UniformReleaseDTO;
import com.masi.employee.service.dto.UniformReturnDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class UniformChangeDetail implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 3424321L;

    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "uniform_name")
    private String uniformName;
    @Column(name = "employee_name")
    private String employeeName;
    @Column(name = "employee_code")
    private String employeeCode;
    @Column(name = "at_date")
    private LocalDate atDate;
    @Column(name = "type")
    private Type type;

    public enum Type {
        STOCKED, RELEASE
    }

}
