package com.masi.employee.service.dto;

import com.masi.employee.domain.UniformRelease;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformReturn} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniformReturnDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private ZonedDateTime date;

    private UUID employeeId;

    private UUID uniformReleaseId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {UniformRelease.class})
    @JsonIgnore
    private UniformReleaseDTO uniformRelease;

    @JsonIgnore
    private Integer quantity = 0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updateAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deleteAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deleteBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<UniformFormDetailRawDTO> uniformFormDetail;

    public record ReturnDetail(UUID uniformId, int quantity) {
    }

    @Schema(allOf = {ReturnDetail.class})
    private ReturnDetail[] returnDetails;

    public int calTotalQuantity() {
        return returnDetails == null ? 0 : Arrays.stream(returnDetails).mapToInt(ReturnDetail::quantity).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniformReturnDTO uniformReturnDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uniformReturnDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }


}
