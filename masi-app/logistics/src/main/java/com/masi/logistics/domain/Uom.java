package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.service.dto.UomDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Uom.
 */
@Table("uom")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Uom implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("create_by")
    private String createBy;

    @Column("update_at")
    private ZonedDateTime updateAt;

    @Column("update_by")
    private String updateBy;

    @Column("delete_at")
    private ZonedDateTime deleteAt;

    @Column("delete_by")
    private String deleteBy;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Uom id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Uom name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public Uom createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Uom createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getUpdateAt() {
        return this.updateAt;
    }

    public Uom updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public Uom updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getDeleteAt() {
        return this.deleteAt;
    }

    public Uom deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public void setDeleteAt(ZonedDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getDeleteBy() {
        return this.deleteBy;
    }

    public Uom deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getCompany() {
        return this.company;
    }

    public Uom company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Uom setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Uom)) {
            return false;
        }
        return getId() != null && getId().equals(((Uom) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Uom{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            ", deleteAt='" + getDeleteAt() + "'" +
            ", deleteBy='" + getDeleteBy() + "'" +
            ", company='" + getCompany() + "'" +
            "}";
    }

    public UomDTO toDto(){
        UomDTO dto = new UomDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setCreateAt(this.getCreateAt());
        dto.setCreateBy(this.getCreateBy());
        dto.setUpdateAt(this.getUpdateAt());
        dto.setUpdateBy(this.getUpdateBy());
        dto.setDeleteAt(this.getDeleteAt());
        dto.setDeleteBy(this.getDeleteBy());
        dto.setCompany(this.getCompany());
        return dto;
    }
}
