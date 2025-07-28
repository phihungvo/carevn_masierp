package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.service.dto.ContractMaterialDTO;
import com.masi.sale.service.dto.ContractProductDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A ContractMaterial.
 */
@Table("contract_product")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class ContractProductFull extends ContractProduct implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Column("product_name")
    private String productName;
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ContractProductDTO toDto() {
        ContractProductDTO dto = super.toDto();
        dto.setProductName(this.getProductName());
        return dto;
    }
}
