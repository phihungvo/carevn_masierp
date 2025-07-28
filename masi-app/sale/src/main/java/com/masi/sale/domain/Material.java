package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.service.dto.MaterialDTO;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Material.
 */
@Table("material")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class Material implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("name_en")
    private String nameEn;

    @Column("note")
    private String note;

    @Column("item_id")
    private UUID itemId;

    @Column("company")
    private String company;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Transient
    private boolean isPersisted;


    // jhipster-needle-entity-add-field - JHipster will add fields here


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Material setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Material)) {
            return false;
        }
        return getId() != null && getId().equals(((Material) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Material{" +
                "id=" + getId() +
                ", productName='" + getName() + "'" +
                ", note='" + getNote() + "'" +
                ", company='" + getCompany() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", createdDate='" + getCreatedDate() + "'" +
                ", isDeleted='" + getIsDeleted() + "'" +
                "}";
    }

    public MaterialDTO toDTO() {
        MaterialDTO dto = new MaterialDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setNameEn(this.getNameEn());
        dto.setNote(this.getNote());
        dto.setCompany(this.getCompany());
        dto.setUpdatedAt(this.getUpdatedAt());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setIsDeleted(this.getIsDeleted());
        dto.setItemId(this.getItemId());
        return dto;
    }
}
