package com.masi.logistics.domain;

import com.carevn.masi.dto.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.service.dto.InventoriesDetailDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A InventoriesDetail.
 */
@Data
@Table("inventories_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesDetail implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("item_id")
    private UUID itemId;

    @Transient
    @JsonIgnoreProperties(value = {"inventoriesDetails"})
    private Item item;

    @Column("inventories_id")
    private UUID inventoriesId;

    @Column("quantity")
    private BigDecimal quantity;

    @Column("price")
    private BigDecimal price;

    @Column("total_price")
    private BigDecimal totalPrice;

    @Column("note")
    private String note;

    @Column("code_uom")
    private String codeUom;

    @Column("uom_id")
    private UUID uomId;

    @Column("uom_name")
    private String uomName;

    @Column("before_item_inventory")
    private Float beforeItemInventory;

    @Column("after_item_inventory")
    private Float afterItemInventory;

    @Column("cost_price")
    private BigDecimal costPrice;

    // VAT
    @Column("vat_rate")
    private Integer vatRate;

    @Column("vat_id")
    private UUID vatId;

    @Column("vat_amount")
    private BigDecimal vatAmount;

    @Column("register_date")
    private ZonedDateTime registerDate;

    @Column("depreciation_date")
    private ZonedDateTime depreciationDate;

    @Column("department_id")
    private UUID departmentId;

    @Column("usage_month")
    private Integer usageMonth;

    @Column("holder")
    private String holder;

    @Column("depreciation_allocation")
    private String depreciationAllocation;

    @Column("expense_account")
    private String expenseAccount;

    @Column("cost_elements")
    private String costElements;

    @Column("unit_price")
    private BigDecimal unitPrice;

    @Column("is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public InventoriesDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public InventoriesDetail code(String code) {
        this.setCode(code);
        return this;
    }

    public InventoriesDetail itemId(UUID itemId) {
        this.setItemId(itemId);
        return this;
    }

    public InventoriesDetail inventoriesId(UUID inventoriesId) {
        this.setInventoriesId(inventoriesId);
        return this;
    }

    public InventoriesDetail quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public InventoriesDetail price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public InventoriesDetail totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public InventoriesDetail note(String note) {
        this.setNote(note);
        return this;
    }

    public InventoriesDetail codeUom(String codeUom) {
        this.setCodeUom(codeUom);
        return this;
    }

    public InventoriesDetail uomId(UUID uomId) {
        this.setUomId(uomId);
        return this;
    }

    public InventoriesDetail uomName(String uomName) {
        this.setUomName(uomName);
        return this;
    }

    public InventoriesDetail beforeItemInventory(Float beforeItemInventory) {
        this.setBeforeItemInventory(beforeItemInventory);
        return this;
    }

    public InventoriesDetail afterItemInventory(Float afterItemInventory) {
        this.setAfterItemInventory(afterItemInventory);
        return this;
    }

    public InventoriesDetail costPrice(BigDecimal costPrice) {
        this.setCostPrice(costPrice);
        return this;
    }

    public InventoriesDetail isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public InventoriesDetail createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public InventoriesDetail createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public InventoriesDetail updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public InventoriesDetail updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public InventoriesDetail deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public InventoriesDetail deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public InventoriesDetail company(String company) {
        this.setCompany(company);
        return this;
    }

    public InventoriesDetail department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public InventoriesDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public InventoriesDetailDTO toDto() {
        InventoriesDetailDTO dto = new InventoriesDetailDTO();
        dto.setId(this.id);
        dto.setCode(this.code);
        dto.setItemId(this.itemId);
        dto.setInventoriesId(this.inventoriesId);
        dto.setQuantity(this.quantity);
        dto.setPrice(this.price);
        dto.setTotalPrice(this.totalPrice);
        dto.setNote(this.note);
        dto.setCodeUom(this.codeUom);
        dto.setUomId(this.uomId);
        dto.setUomName(this.uomName);
        dto.setBeforeItemInventory(this.beforeItemInventory);
        dto.setAfterItemInventory(this.afterItemInventory);
        dto.setCostPrice(this.costPrice);
        dto.setVatRate(this.vatRate);
        dto.setVatId(this.vatId);
        dto.setVatAmount(this.vatAmount);
        dto.setRegisterDate(this.registerDate);
        dto.setDepreciationDate(this.depreciationDate);
        dto.setDepartmentId(this.departmentId);
        dto.setUsageMonth(this.usageMonth);
        dto.setHolder(this.holder);
        dto.setDepreciationAllocation(this.depreciationAllocation);
        dto.setExpenseAccount(this.expenseAccount);
        dto.setCostElements(this.costElements);
        dto.setUnitPrice(this.unitPrice);
        dto.setIsDeleted(this.isDeleted);
        dto.setCreatedAt(this.createdAt);
        dto.setCreatedBy(this.createdBy);
        dto.setUpdatedAt(this.updatedAt);
        dto.setUpdatedBy(this.updatedBy);
        dto.setDeletedAt(this.deletedAt);
        dto.setDeletedBy(this.deletedBy);
        dto.setCompany(this.company);
        dto.setDepartment(this.department);
        return dto;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
