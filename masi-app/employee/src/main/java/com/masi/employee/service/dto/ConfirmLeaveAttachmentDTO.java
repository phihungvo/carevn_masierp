package com.masi.employee.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.ConfirmLeaveAttachment} entity.
 */

@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class ConfirmLeaveAttachmentDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private UUID fileId;

    private ConfirmLeaveDTO confirmLeave;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfirmLeaveAttachmentDTO confirmLeaveAttachmentDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, confirmLeaveAttachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }


}
