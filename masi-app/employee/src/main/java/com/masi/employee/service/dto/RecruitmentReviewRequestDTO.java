package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.PositionEmployee;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.RecruitmentReviewRequest} entity.
 */
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentReviewRequestDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    private PositionEmployee position;

    @NotNull(message = "must not be null")
    private UUID employeeId;

    @NotNull(message = "must not be null")
    private UUID requestId;

    @NotNull(message = "must not be null")
    private String company;

    private String approvalSignFile;

    private FileAttachmentDTO approvalSignFileAttachment;
    private String rejectNote;

    private ZonedDateTime createdDate;

    private String updatedBy;

    private ZonedDateTime updatedAt;

    private Boolean result;

    private Boolean isDeleted;
    @Transient
    private String employeeName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentReviewRequestDTO recruitmentReviewRequestDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recruitmentReviewRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
