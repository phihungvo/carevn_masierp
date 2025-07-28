package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.service.dto.ContractMaterialDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ContractMaterial.
 */
@Data
@Table("contract_material")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractMaterial implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("id_contract")
    private UUID idContract;

    @NotNull(message = "must not be null")
    @Column("id_material")
    private UUID idMaterial;

    @Column("price")
    private Double price;

    @Column("quantity")
    private Double quantity;

    @Column("unit")
    private String unit;

    @Column("protein_parameters")
    private String proteinParameters;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Transient
    private boolean isPersisted;

    @Column("order_id")
    private UUID orderId;

    @Transient
    @JsonIgnoreProperties(value = {"contractProduct"}, allowSetters = true)
    private Set<Material> materials = new HashSet<>();

    @Transient
    private  Material material;

    @Column("delivered_quantity")
    private Float deliveredQuantity=0f;


    @Column("manufacture_order_id")
    private UUID manufactureOrderId;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ContractMaterial id(UUID id) {
        this.setId(id);
        return this;
    }

    public ContractMaterial idContract(UUID idContract) {
        this.setIdContract(idContract);
        return this;
    }

    public ContractMaterial idMaterial(UUID idMaterial) {
        this.setIdMaterial(idMaterial);
        return this;
    }

    public ContractMaterial price(Double price) {
        this.setPrice(price);
        return this;
    }

    public ContractMaterial quantity(Double quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public ContractMaterial unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public ContractMaterial proteinParameters(String proteinParameters) {
        this.setProteinParameters(proteinParameters);
        return this;
    }

    public ContractMaterial company(String company) {
        this.setCompany(company);
        return this;
    }

    public ContractMaterial department(String department) {
        this.setDepartment(department);
        return this;
    }

    public ContractMaterial updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ContractMaterial createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public ContractMaterial isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ContractMaterial setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<Material> getProducts() {
        return this.materials;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public ContractMaterialDTO toDto() {
        ContractMaterialDTO dto = new ContractMaterialDTO();
        dto.setId(getId());
        dto.setIdContract(this.getIdContract());
        dto.setIdMaterial(this.getIdMaterial());
        dto.setPrice(this.getPrice());
        dto.setQuantity(this.getQuantity());
        dto.setUnit(this.getUnit());
        dto.setProteinParameters(this.getProteinParameters());
        dto.setCompany(this.getCompany());
        dto.setDepartment(this.getDepartment());
        dto.setUpdatedAt(this.getUpdatedAt());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setIsDeleted(this.getIsDeleted());
        dto.setOrderId(this.getOrderId());
        dto.setManufactureOrderId(this.getManufactureOrderId());
        dto.setDeliveredQuantity(this.getDeliveredQuantity());
        if (this.material != null) {
            dto.setMaterial(this.material.toDTO());
        }
        return dto;
    }
}
