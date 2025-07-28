package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.ProfileAttachmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.ProfileAttachment} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileAttachmentDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id = UUID.randomUUID();

    private UUID employeeProfileId;


    private ProfileAttachmentType type;

    private String path;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = FileAttachmentDTO.class)
    private FileAttachmentDTO fileAttachment;

    public boolean getChecked(){
        return fileAttachment!=null && StringUtils.isNotBlank(fileAttachment.getName());
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted=false;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileAttachmentDTO profileAttachmentDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profileAttachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }


}
