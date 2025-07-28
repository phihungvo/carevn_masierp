package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.UniformOrderProcessStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformOrder} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderHandleProcessDTO implements Serializable {

    public enum UniformOrderProcessStatusHandle {
        APPROVED,
        REJECTED
    }

    private UniformOrderProcessStatusHandle status;

    private String fileId;

    private String fileName;

    private String reason;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID approverId;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updateAt;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UniformOrderDTO uniformOrderDTO;


    public UniformOrderProcessDTO toDto(UUID processId) {
        var uniformOrderProcessDTO = new UniformOrderProcessDTO();
        uniformOrderProcessDTO.setStatus(UniformOrderProcessStatus.valueOf(this.status.name()));
        uniformOrderProcessDTO.setFileId(this.fileId);
        uniformOrderProcessDTO.setFileName(this.fileName);
        uniformOrderProcessDTO.setReason(this.reason);
        uniformOrderProcessDTO.setApproverId(this.approverId);
        uniformOrderProcessDTO.setUpdateBy(this.updateBy);
        uniformOrderProcessDTO.setUpdateAt(this.updateAt);
        uniformOrderProcessDTO.setUniformOrder(this.uniformOrderDTO);
        uniformOrderProcessDTO.setId(processId);

        return uniformOrderProcessDTO;
    }


}
