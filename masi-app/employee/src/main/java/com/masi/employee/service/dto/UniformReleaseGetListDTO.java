package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.UniformOrderStatus;
import com.masi.employee.domain.enumeration.UniformReleaseType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformOrder} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformReleaseGetListDTO implements Serializable {

    private LocalDate startDate;

    private LocalDate endDate;

    private List<UniformReleaseType> type;

    private List<UUID> uniformId = new ArrayList<>();

    private List<UUID> employeeIds = new ArrayList<>();
    private String search;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private List<UUID> releaseId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private String company;

}
