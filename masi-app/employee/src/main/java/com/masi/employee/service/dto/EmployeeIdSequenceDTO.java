package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.util.UUID;

@Data
@Builder
public class EmployeeIdSequenceDTO {
    private Gender gender;
    private String workspaceId;
    private String nextEmployeeId;
    private Integer currentSequence;
    @JsonIgnore
    private String javaFormat;

    @JsonIgnore
    public String getAndIncrease() {
        return String.format(javaFormat, currentSequence++);
    }


}
