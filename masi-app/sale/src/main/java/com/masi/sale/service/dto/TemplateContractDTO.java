package com.masi.sale.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.TemplateContract} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateContractDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private String nameFile;

    @NotNull(message = "must not be null")
    private String company;

    private String department;

    private Boolean isDeleted;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateContractDTO)) {
            return false;
        }

        TemplateContractDTO templateContractDTO = (TemplateContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, templateContractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateContractDTO{" +
            "id='" + getId() + "'" +
            ", nameFile='" + getNameFile() + "'" +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
