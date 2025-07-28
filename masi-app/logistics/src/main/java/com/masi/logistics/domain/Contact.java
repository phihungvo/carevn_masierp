package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Contact.
 */
@Table("contact")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contact implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("supplier_id")
    private UUID supplierId;

    @Column("name")
    private String name;

    @Column("email")
    private String email;

    @Column("phone")
    private String phone;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("position")
    private String position;

    @Column("contact_info")
    private String contactInfo;

    @Column("is_active")
    private Boolean isActive;

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
    @JsonIgnoreProperties(value = { "contact" }, allowSetters = true)
    private Set<ContactGift> contactGifts = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "contacts" }, allowSetters = true)
    private ContactType contactType;

    @Column("contact_type_id")
    private UUID contactTypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Contact id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSupplierId() {
        return this.supplierId;
    }

    public Contact supplierId(UUID supplierId) {
        this.setSupplierId(supplierId);
        return this;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return this.name;
    }

    public Contact name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Contact email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Contact phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Contact birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPosition() {
        return this.position;
    }

    public Contact position(String position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public Contact contactInfo(String contactInfo) {
        this.setContactInfo(contactInfo);
        return this;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Contact isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCompany() {
        return this.company;
    }

    public Contact company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return this.department;
    }

    public Contact department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Contact createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Contact createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Contact updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Contact updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedBy() {
        return this.deletedBy;
    }

    public Contact deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public ZonedDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public Contact deletedAt(ZonedDateTime deletedAt) {
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

    public Contact setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<ContactGift> getContactGifts() {
        return this.contactGifts;
    }

    public void setContactGifts(Set<ContactGift> contactGifts) {
        if (this.contactGifts != null) {
            this.contactGifts.forEach(i -> i.setContact(null));
        }
        if (contactGifts != null) {
            contactGifts.forEach(i -> i.setContact(this));
        }
        this.contactGifts = contactGifts;
    }

    public Contact contactGifts(Set<ContactGift> contactGifts) {
        this.setContactGifts(contactGifts);
        return this;
    }

    public Contact addContactGift(ContactGift contactGift) {
        this.contactGifts.add(contactGift);
        contactGift.setContact(this);
        return this;
    }

    public Contact removeContactGift(ContactGift contactGift) {
        this.contactGifts.remove(contactGift);
        contactGift.setContact(null);
        return this;
    }

    public ContactType getContactType() {
        return this.contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
        this.contactTypeId = contactType != null ? contactType.getId() : null;
    }

    public Contact contactType(ContactType contactType) {
        this.setContactType(contactType);
        return this;
    }

    public UUID getContactTypeId() {
        return this.contactTypeId;
    }

    public void setContactTypeId(UUID contactType) {
        this.contactTypeId = contactType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return getId() != null && getId().equals(((Contact) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", supplierId='" + getSupplierId() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", position='" + getPosition() + "'" +
            ", contactInfo='" + getContactInfo() + "'" +
            ", isActive='" + getIsActive() + "'" +
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
