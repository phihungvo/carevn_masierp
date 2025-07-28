package com.masi.employee.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.masi.employee.domain.DocumentSequence} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentSequenceDTO implements Serializable {

    private Long id;

    private String entityName;

    private Integer currentSequence;

    private String company;

    private String department;

    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getCurrentSequence() {
        return currentSequence;
    }

    public void setCurrentSequence(Integer currentSequence) {
        this.currentSequence = currentSequence;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentSequenceDTO)) {
            return false;
        }

        DocumentSequenceDTO documentSequenceDTO = (DocumentSequenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentSequenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentSequenceDTO{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", currentSequence=" + getCurrentSequence() +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
