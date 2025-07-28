package com.masi.utility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.utility.domain.enumeration.CalenderType;
import com.masi.utility.domain.enumeration.HolidayType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HolidayConfig.
 */
@Table("holiday_config")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HolidayConfig implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("type")
    private HolidayType type;

    @Column("calender_type")
    private CalenderType calenderType;

    @Column("date")
    private LocalDate date;

    @Column("description")
    private String description;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public HolidayConfig id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HolidayConfig name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HolidayType getType() {
        return this.type;
    }

    public HolidayConfig type(HolidayType type) {
        this.setType(type);
        return this;
    }

    public void setType(HolidayType type) {
        this.type = type;
    }

    public CalenderType getCalenderType() {
        return this.calenderType;
    }

    public HolidayConfig calenderType(CalenderType calenderType) {
        this.setCalenderType(calenderType);
        return this;
    }

    public void setCalenderType(CalenderType calenderType) {
        this.calenderType = calenderType;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public HolidayConfig date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public HolidayConfig description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public HolidayConfig createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public HolidayConfig updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public HolidayConfig setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HolidayConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((HolidayConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HolidayConfig{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", calenderType='" + getCalenderType() + "'" +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
