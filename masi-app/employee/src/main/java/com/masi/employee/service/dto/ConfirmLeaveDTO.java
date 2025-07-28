package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.ConfirmLeave} entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfirmLeaveDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    private LocalDate submissionDate;

    private String reason;

    private String recruitmentSolution;

    private String hrSolution;

    private LocalDate leaveDate;

    private Collection<UUID> fileAttachmentIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = FileAttachmentDTO.class)
    private Collection<FileAttachmentDTO> fileAttachments;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfirmLeaveDTO confirmLeaveDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, confirmLeaveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
