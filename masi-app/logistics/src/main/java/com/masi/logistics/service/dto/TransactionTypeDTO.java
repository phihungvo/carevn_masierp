package com.masi.logistics.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.TransactionType} entity.
 */
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionTypeDTO implements Serializable {

    private UUID id;

    private String code;

    private String attribute;

    private String name;

    private Boolean isDeleted;

    private ZonedDateTime createdAt;

    private String createdBy;

    private ZonedDateTime updatedAt;

    private String updatedBy;

    private ZonedDateTime deletedAt;

    private String deletedBy;

    private String company;

    private String department;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionTypeDTO)) {
            return false;
        }

        TransactionTypeDTO transactionTypeDTO = (TransactionTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionTypeDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", attribute='" + getAttribute() + "'" +
            ", name='" + getName() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", deletedBy='" + getDeletedBy() + "'" +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            "}";
    }
}
