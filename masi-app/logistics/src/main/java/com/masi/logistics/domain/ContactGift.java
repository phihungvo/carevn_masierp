package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.ContactGiftStatus;
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
 * A ContactGift.
 */
@Table("contact_gift")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactGift implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("gift_name")
    private String giftName;

    @Column("value")
    private Integer value;

    @Column("is_giving")
    private Boolean isGiving;

    @Column("status")
    private ContactGiftStatus status;

    @Column("expected_date")
    private ZonedDateTime expectedDate;

    @Column("date_of_giving")
    private ZonedDateTime dateOfGiving;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @NotNull(message = "must not be null")
    @Column("created_by")
    private String createdBy;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "contactGifts", "contactType" }, allowSetters = true)
    private Contact contact;

    @Column("contact_id")
    private UUID contactId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ContactGift id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ContactGift name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ContactGift description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGiftName() {
        return this.giftName;
    }

    public ContactGift giftName(String giftName) {
        this.setGiftName(giftName);
        return this;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public Integer getValue() {
        return this.value;
    }

    public ContactGift value(Integer value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Boolean getIsGiving() {
        return this.isGiving;
    }

    public ContactGift isGiving(Boolean isGiving) {
        this.setIsGiving(isGiving);
        return this;
    }

    public void setIsGiving(Boolean isGiving) {
        this.isGiving = isGiving;
    }

    public ContactGiftStatus getStatus() {
        return this.status;
    }

    public ContactGift status(ContactGiftStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ContactGiftStatus status) {
        this.status = status;
    }

    public ZonedDateTime getExpectedDate() {
        return this.expectedDate;
    }

    public ContactGift expectedDate(ZonedDateTime expectedDate) {
        this.setExpectedDate(expectedDate);
        return this;
    }

    public void setExpectedDate(ZonedDateTime expectedDate) {
        this.expectedDate = expectedDate;
    }

    public ZonedDateTime getDateOfGiving() {
        return this.dateOfGiving;
    }

    public ContactGift dateOfGiving(ZonedDateTime dateOfGiving) {
        this.setDateOfGiving(dateOfGiving);
        return this;
    }

    public void setDateOfGiving(ZonedDateTime dateOfGiving) {
        this.dateOfGiving = dateOfGiving;
    }

    public String getCompany() {
        return this.company;
    }

    public ContactGift company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return this.department;
    }

    public ContactGift department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ContactGift createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public ContactGift createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ContactGift updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public ContactGift updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedBy() {
        return this.deletedBy;
    }

    public ContactGift deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public ZonedDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public ContactGift deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(ZonedDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ContactGift setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        this.contactId = contact != null ? contact.getId() : null;
    }

    public ContactGift contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public UUID getContactId() {
        return this.contactId;
    }

    public void setContactId(UUID contact) {
        this.contactId = contact;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactGift)) {
            return false;
        }
        return getId() != null && getId().equals(((ContactGift) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactGift{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", giftName='" + getGiftName() + "'" +
            ", value=" + getValue() +
            ", isGiving='" + getIsGiving() + "'" +
            ", status='" + getStatus() + "'" +
            ", expectedDate='" + getExpectedDate() + "'" +
            ", dateOfGiving='" + getDateOfGiving() + "'" +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedBy='" + getDeletedBy() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
