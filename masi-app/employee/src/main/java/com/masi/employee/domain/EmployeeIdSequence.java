package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masi.employee.domain.enumeration.Gender;
import com.masi.employee.service.dto.EmployeeIdSequenceDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A EmployeeIdSequence.
 */
@Table("employee_id_sequence")
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeIdSequence implements Serializable, Persistable<Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("current_sequence")
    private Integer currentSequence;

    @NotNull(message = "must not be null")
    @Column("gender")
    private Gender gender;

    @NotNull(message = "must not be null")
    @Column("workspace_id")
    private String workspaceId;

    @Column("java_format")
    private String javaFormat;
    // jhipster-needle-entity-add-field - JHipster will add fields here



    public EmployeeIdSequence id(Long id) {
        this.setId(id);
        return this;
    }


    public EmployeeIdSequence currentSequence(Integer currentSequence) {
        this.setCurrentSequence(currentSequence);
        return this;
    }


    public EmployeeIdSequence gender(Gender gender) {
        this.setGender(gender);
        return this;
    }


    public EmployeeIdSequence workspaceId(String workspaceId) {
        this.setWorkspaceId(workspaceId);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeIdSequence)) {
            return false;
        }
        return getId() != null && getId().equals(((EmployeeIdSequence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeIdSequence{" +
            "id=" + getId() +
            ", currentSequence=" + getCurrentSequence() +
            ", gender='" + getGender() + "'" +
            ", workspaceId='" + getWorkspaceId() + "'" +
            "}";
    }

    public String getNextEmployeeId() {
        return String.format(javaFormat, currentSequence + 1);
    }

    @JsonIgnore
    public String getAndIncrease() {
        return String.format(javaFormat, ++currentSequence);
    }

    public EmployeeIdSequenceDTO toDTO() {
        return EmployeeIdSequenceDTO.builder()
            .nextEmployeeId(getNextEmployeeId())
            .gender(gender)
            .currentSequence(currentSequence)
            .javaFormat(javaFormat)
            .workspaceId(workspaceId)
            .build();

    }
    public EmployeeIdSequence reset() {
        currentSequence = 0;
        return this;
    }

    public EmployeeIdSequence setIsPersisted() {
        this.isPersisted = true;
        return this;
    }
    @Transient
    private boolean isPersisted;

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }
}
