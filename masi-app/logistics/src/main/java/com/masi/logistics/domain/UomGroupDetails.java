package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A UomGroupDetails.
 */
@Table("uom_group_details")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UomGroupDetails implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("base_qty")
    private Integer baseQty;

    @Column("alt_qty")
    private Integer altQty;

    @Column("active")
    private Boolean active;

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

    @Transient
    private Uom baseUom;

    @Transient
    private Uom altUom;

    @Transient
    @JsonIgnoreProperties(value = { "uomGroupDetails", "baseUom" }, allowSetters = true)
    private UomGroup uomGroup;

    @Column("base_uom_id")
    private UUID baseUomId;

    @Column("alt_uom_id")
    private UUID altUomId;

    @Column("uom_group_id")
    private UUID uomGroupId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public UomGroupDetails id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UomGroupDetails name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBaseQty() {
        return this.baseQty;
    }

    public UomGroupDetails baseQty(Integer baseQty) {
        this.setBaseQty(baseQty);
        return this;
    }

    public void setBaseQty(Integer baseQty) {
        this.baseQty = baseQty;
    }

    public Integer getAltQty() {
        return this.altQty;
    }

    public UomGroupDetails altQty(Integer altQty) {
        this.setAltQty(altQty);
        return this;
    }

    public void setAltQty(Integer altQty) {
        this.altQty = altQty;
    }

    public Boolean getActive() {
        return this.active;
    }

    public UomGroupDetails active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public UomGroupDetails createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public UomGroupDetails createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getUpdateAt() {
        return this.updateAt;
    }

    public UomGroupDetails updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public UomGroupDetails updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getDeleteAt() {
        return this.deleteAt;
    }

    public UomGroupDetails deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public void setDeleteAt(ZonedDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getDeleteBy() {
        return this.deleteBy;
    }

    public UomGroupDetails deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getCompany() {
        return this.company;
    }

    public UomGroupDetails company(String company) {
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

    public UomGroupDetails setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Uom getBaseUom() {
        return this.baseUom;
    }

    public void setBaseUom(Uom uom) {
        this.baseUom = uom;
        this.baseUomId = uom != null ? uom.getId() : null;
    }

    public UomGroupDetails baseUom(Uom uom) {
        this.setBaseUom(uom);
        return this;
    }

    public Uom getAltUom() {
        return this.altUom;
    }

    public void setAltUom(Uom uom) {
        this.altUom = uom;
        this.altUomId = uom != null ? uom.getId() : null;
    }

    public UomGroupDetails altUom(Uom uom) {
        this.setAltUom(uom);
        return this;
    }

    public UomGroup getUomGroup() {
        return this.uomGroup;
    }

    public void setUomGroup(UomGroup uomGroup) {
        this.uomGroup = uomGroup;
        this.uomGroupId = uomGroup != null ? uomGroup.getId() : null;
    }

    public UomGroupDetails uomGroup(UomGroup uomGroup) {
        this.setUomGroup(uomGroup);
        return this;
    }

    public UUID getBaseUomId() {
        return this.baseUomId;
    }

    public void setBaseUomId(UUID uom) {
        this.baseUomId = uom;
    }

    public UUID getAltUomId() {
        return this.altUomId;
    }

    public void setAltUomId(UUID uom) {
        this.altUomId = uom;
    }

    public UUID getUomGroupId() {
        return this.uomGroupId;
    }

    public void setUomGroupId(UUID uomGroup) {
        this.uomGroupId = uomGroup;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UomGroupDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((UomGroupDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UomGroupDetails{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", baseQty=" + getBaseQty() +
            ", altQty=" + getAltQty() +
            ", active='" + getActive() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            ", deleteAt='" + getDeleteAt() + "'" +
            ", deleteBy='" + getDeleteBy() + "'" +
            ", company='" + getCompany() + "'" +
            "}";
    }
}
