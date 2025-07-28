package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.UniformOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformOrder} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderGetListDTO implements Serializable {

    private String name;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private List<UniformOrderStatus> status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private String company;

}
