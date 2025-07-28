package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.service.dto.ContractMaterialDTO;
import com.masi.sale.service.dto.ContractProductDTO;
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
 * A ContractProduct.
 */
@Table("contract_product")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractProduct implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("id_contract")
    private UUID idContract;

    @NotNull(message = "must not be null")
    @Column("id_product")
    private UUID idProduct;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ContractProduct id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIdContract() {
        return this.idContract;
    }

    public ContractProduct idContract(UUID idContract) {
        this.setIdContract(idContract);
        return this;
    }

    public void setIdContract(UUID idContract) {
        this.idContract = idContract;
    }

    public UUID getIdProduct() {
        return this.idProduct;
    }

    public ContractProduct idProduct(UUID idProduct) {
        this.setIdProduct(idProduct);
        return this;
    }

    public void setIdProduct(UUID idProduct) {
        this.idProduct = idProduct;
    }

    public Double getPrice() {
        return this.price;
    }

    public ContractProduct price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public ContractProduct quantity(Double quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return this.unit;
    }

    public ContractProduct unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProteinParameters() {
        return this.proteinParameters;
    }

    public ContractProduct proteinParameters(String proteinParameters) {
        this.setProteinParameters(proteinParameters);
        return this;
    }

    public void setProteinParameters(String proteinParameters) {
        this.proteinParameters = proteinParameters;
    }

    public String getCompany() {
        return this.company;
    }

    public ContractProduct company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return this.department;
    }

    public ContractProduct department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public ContractProduct updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public ContractProduct createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public ContractProduct isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ContractProduct setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractProduct)) {
            return false;
        }
        return getId() != null && getId().equals(((ContractProduct) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractProduct{" +
            "id=" + getId() +
            ", idContract='" + getIdContract() + "'" +
            ", idProduct='" + getIdProduct() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", unit='" + getUnit() + "'" +
            ", proteinParameters='" + getProteinParameters() + "'" +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }

    public ContractProductDTO toDto() {
      ContractProductDTO dto = new ContractProductDTO();
      dto.setId(getId());
      dto.setIdProduct(getIdProduct());
      dto.setPrice(getPrice());
      dto.setQuantity(getQuantity());
      dto.setUnit(getUnit());
      dto.setProteinParameters(getProteinParameters());
      dto.setCompany(getCompany());
      dto.setDepartment(getDepartment());
      dto.setUpdatedAt(getUpdatedAt());
      dto.setCreatedDate(getCreatedDate());
      dto.setIsDeleted(getIsDeleted());
      return dto;
    }
}
