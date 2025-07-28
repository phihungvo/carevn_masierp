package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.domain.enumeration.ContractStatus;
import com.masi.sale.domain.enumeration.OrderStatus;
import com.masi.sale.service.dto.ContractDTO;
import com.masi.sale.service.dto.ContractMaterialDTO;
import com.masi.sale.service.dto.CustomerDTO;
import com.masi.sale.service.dto.OrderDTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A ContractMaterial.
 */
@EqualsAndHashCode(callSuper = true)
@Table("contract_material")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class ContractMaterialFull extends ContractMaterial implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column("material_name")
    private String materialName;
    // jhipster-needle-entity-add-field - JHipster will add fields here

    @Column("item_id")
    private UUID itemId;

    // Map data order
    @Column("order_code")
    private String orderCode;

    @Column("date_order")
    private LocalDate dateOrder;

    @Column("status_order")
    private OrderStatus statusOrder;

    @Column("quantity_order")
    private String quantityOrder;

    // Map data contract

    @Column("status")
    private ContractStatus statusContract;

    @Column("contract_name")
    private String contractName;

    // Map data customer

    @Column("customer_code")
    private String customerCode;

    @Column("company_name")
    private String companyName;


    public ContractMaterialDTO toDtoMapOrderAndContract() {
        ContractMaterialDTO dto = super.toDto();
        dto.setMaterialName(this.getMaterialName());
        dto.setItemId(this.getItemId());

        if (dto.getOrderId() != null) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderCode(this.orderCode);
            orderDTO.setDateOrder(this.dateOrder);
            orderDTO.setStatus(this.statusOrder);
            orderDTO.setQuantity(this.quantityOrder);
            dto.setOrder(orderDTO);
        }

        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setContractName(this.contractName);
        contractDTO.setStatus(this.statusContract);
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerCode(this.customerCode);
        customerDTO.setCompanyName(this.companyName);
        contractDTO.setCustomer(customerDTO);
        dto.setContract(contractDTO);


        return dto;
    }

    public ContractMaterialDTO toDto() {
        ContractMaterialDTO dto = super.toDto();
        dto.setMaterialName(this.getMaterialName());
        dto.setItemId(this.getItemId());
        return dto;
    }
}
