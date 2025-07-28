package com.masi.utility.domain;

import com.masi.utility.domain.enumeration.DataType;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Config.
 */

@Table("config")
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Config implements Serializable, Persistable<Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("key")
    private String key;

    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("value")
    private String value;

    @NotNull(message = "must not be null")
    @Column("type")
    private DataType type;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("company")
    private String company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Config id(Long id) {
        this.setId(id);
        return this;
    }

    public Config key(String key) {
        this.setKey(key);
        return this;
    }

    public Config description(String description) {
        this.setDescription(description);
        return this;
    }

    public Config value(String value) {
        this.setValue(value);
        return this;
    }

    public Config type(DataType type) {
        this.setType(type);
        return this;
    }

    public Config updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Config updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Config company(String company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Config)) {
            return false;
        }
        return getId() != null && getId().equals(((Config) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Config{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", description='" + getDescription() + "'" +
            ", value='" + getValue() + "'" +
            ", type='" + getType() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", company='" + getCompany() + "'" +
            "}";
    }

    @Transient
    private boolean isPersisted=false;

    @Transient
    public Config setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

}
