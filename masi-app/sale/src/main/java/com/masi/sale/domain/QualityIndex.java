package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.service.dto.CreateQuantityIndexDto;

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
 * A QualityIndex.
 */
@Table("quality_index")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QualityIndex implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("value")
    private String value;

    @NotNull(message = "must not be null")
    @Column("order_id")
    private UUID orderId;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public QualityIndex id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public QualityIndex name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public QualityIndex value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public QualityIndex orderId(UUID orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public QualityIndex createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public QualityIndex setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QualityIndex)) {
            return false;
        }
        return getId() != null && getId().equals(((QualityIndex) o).getId());
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
        return "QualityIndex{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", value='" + getValue() + "'" +
                ", orderId='" + getOrderId() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                "}";
    }

    public CreateQuantityIndexDto toDTO() {
        CreateQuantityIndexDto createQuantityIndexDto = new CreateQuantityIndexDto();
        createQuantityIndexDto.setName(this.name);
        createQuantityIndexDto.setValue(this.value);
        return createQuantityIndexDto;
    }
}
