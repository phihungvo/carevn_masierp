package com.masi.employee.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DocumentSequence.
 */
@Table("document_sequence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentSequence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("entity_name")
    private String entityName;

    @Column("current_sequence")
    private Integer currentSequence;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentSequence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public DocumentSequence entityName(String entityName) {
        this.setEntityName(entityName);
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getCurrentSequence() {
        return this.currentSequence;
    }

    public DocumentSequence currentSequence(Integer currentSequence) {
        this.setCurrentSequence(currentSequence);
        return this;
    }

    public void setCurrentSequence(Integer currentSequence) {
        this.currentSequence = currentSequence;
    }

    public String getCompany() {
        return this.company;
    }

    public DocumentSequence company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return this.department;
    }

    public DocumentSequence department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public DocumentSequence isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentSequence)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentSequence) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentSequence{" +
                "id=" + getId() +
                ", entityName='" + getEntityName() + "'" +
                ", currentSequence=" + getCurrentSequence() +
                ", company='" + getCompany() + "'" +
                ", department='" + getDepartment() + "'" +
                ", isActive='" + getIsActive() + "'" +
                "}";
    }

    public DocumentSequence initNewSequence(String entityName, String company) {
        DocumentSequence sequence = new DocumentSequence();
        sequence.setEntityName(entityName);
        sequence.setCurrentSequence(1);
        sequence.setCompany(company);
        sequence.setIsActive(true);
        return sequence;
    }
}
