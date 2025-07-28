package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UniformFormDetail.
 */
@Table("uniform_form_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniformFormDetail implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("quantity")
    private Integer quantity;

    @Column("returned_quantity")
    private Integer quantityChange;

    @Column("actual_price")
    private Double actualPrice;

    @Column("uom_id")
    private UUID uomId;

    @Column("uom_name")
    private String uomName;

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
    @JsonIgnoreProperties(value = { "uniformFormDetails", "uniformStocks" }, allowSetters = true)
    private Uniform uniform;

    @Transient
    @JsonIgnoreProperties(value = { "uniformFormDetails" }, allowSetters = true)
    private UniformRelease uniformRelease;

    @Transient
    @JsonIgnoreProperties(value = { "uniformFormDetails" }, allowSetters = true)
    private UniformOrder uniformOrder;

    @Transient
    @JsonIgnoreProperties(value = { "uniformFormDetails" }, allowSetters = true)
    private UniformReturn uniformReturn;

    @Transient
    @JsonIgnoreProperties(value = { "uniformFormDetails" }, allowSetters = true)
    private UniformOrderStock uniformOrderStock;


    @Column("uniform_id")
    private UUID uniformId;

    @Column("uniform_release_id")
    private UUID uniformReleaseId;

    @Column("uniform_order_id")
    private UUID uniformOrderId;

    @Column("uniform_return_id")
    private UUID uniformReturnId;

    @Column("uniform_order_stock_id")
    private UUID uniformOrderStockId;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public UniformFormDetail id(UUID id) {
        this.setId(id);
        return this;
    }



    public UniformFormDetail quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }



    public UniformFormDetail createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }



    public UniformFormDetail createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }


    public UniformFormDetail updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public UniformFormDetail updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }



    public UniformFormDetail deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }


    public UniformFormDetail deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }


    public UniformFormDetail company(String company) {
        this.setCompany(company);
        return this;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UniformFormDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public UniformFormDetail uniform(Uniform uniform) {
        this.setUniform(uniform);
        return this;
    }


    public UniformFormDetail uniformRelease(UniformRelease uniformRelease) {
        this.setUniformRelease(uniformRelease);
        return this;
    }


    public UniformFormDetail uniformOrder(UniformOrder uniformOrder) {
        this.setUniformOrder(uniformOrder);
        return this;
    }


    public UniformFormDetail uniformReturn(UniformReturn uniformReturn) {
        this.setUniformReturn(uniformReturn);
        return this;
    }

    public UniformFormDetail returnedQuantity(Integer returnedQuantity) {
        this.setQuantityChange(returnedQuantity);
        return this;
    }



    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


}
