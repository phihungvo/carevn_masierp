package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.*;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * A EmployeeProfile.
 */
@Data
@Table("employee_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonIgnoreProperties(value = {"new"})

public class EmployeeProfileAndTimeKeeping extends EmployeeProfile implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Column("check_in")
    private ZonedDateTime CheckIn;}
