package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.domain.enumeration.ManufactureOrderType;
import com.masi.production.domain.enumeration.StatusEntity;
import com.masi.production.service.dto.*;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.CollectionUtils;

/**
 * A ManufactureOrder.
 */
@Slf4j
@Data
@Table("manufacture_order")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManufactureOrder implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("code")
    private String code;

    @Column("from_date")
    private LocalDate fromDate;

    @Column("manufacture_order_type")
    private String manufactureOrderType;

    @Column("to_date")
    private LocalDate toDate;

    @Column("status")
    private StatusEntity status;

    @Column("order_id")
    private UUID orderId;

    @Column("is_active")
    private Boolean isActive;

    @Column("type_protein")
    private String typeProtein;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("created_by")
    private UUID createdBy;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Column("quality_check_sample_id")
    private UUID qualityCheckSampleId;
    @Transient
    private QualityCheckSample qualityCheckSample;

    @Column("production_status")
    private String productionStatus;

    @Column("production_standard_id")
    private UUID productionStandardId;

    @Column("production_quantity")
    private Float productionQuantity;

    @Column("production_routing_id")
    private UUID productionRoutingId;
    @Transient
    private ProductRouting productionRouting;

    @Column("inventory_id")
    private UUID inventoryId;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = {"workItem", "manufactureOrder"}, allowSetters = true)
    private Collection<WorkOrder> workOrders = new HashSet<>();

    @Column("material_id")
    private UUID materialId;

    @Column("percent_protein")
    private String percentProtein;

    @Column("attributes")
    private Json attributes;

    @Column("product_maintain_id")
    private UUID productMaintainId;
    @Transient
    private ProductMaintain productMaintain;

    @Column("product_package_id")
    private UUID productPackageId;
    @Transient
    private ProductPackage productPackage;
    // jhipster-needle-entity-add-field - JHipster will add fields here

    @Column("order_item_id")
    private UUID orderItemId;

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ManufactureOrder setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public ManufactureOrderDTO toDto() {
        ManufactureOrderDTO dto = new ManufactureOrderDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setCode(this.code);
        dto.setFromDate(this.getFromDate());
//        dto.setTypeProtein("abc");
//        dto.setTypeProtein(this.getTypeProtein());
//        System.out.println("\n\n\n\n\n\n\n\n\n\n\n " + this.getTypeProtein());

        if (Objects.nonNull(this.getFromDate())) {
            dto.setZonedFromDate(ZonedDateTime.of(this.getFromDate().atStartOfDay(), ZoneId.systemDefault()));
        }
        dto.setToDate(this.getToDate());
        if (Objects.nonNull(this.getToDate())) {
            dto.setZonedToDate(ZonedDateTime.of(this.getToDate().atStartOfDay(), ZoneId.systemDefault()));
        }
        dto.setStatus(this.getStatus());
        dto.setOrderId(this.getOrderId());
        dto.setIsActive(this.getIsActive());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setLastUpdated(this.getLastUpdated());
        dto.setProductionStandardId(this.productionStandardId);
        dto.setQualityCheckSampleId(this.qualityCheckSampleId);
        dto.setProductionStatus(this.productionStatus);
        dto.setProductionQuantity(this.productionQuantity);
        dto.setPercentProtein(this.percentProtein);
        dto.setMaterialId(this.materialId);
        dto.setManufactureOrderType(this.manufactureOrderType);
        if (!CollectionUtils.isEmpty(this.workOrders)) {
            dto.setWorkOrders(this.getWorkOrders().stream().map(WorkOrder::toDto).collect(Collectors.toList()));
            var list = new LinkedList<>(dto.getWorkOrders());
            log.info("Sort by order index {}", this.getManufactureOrderType());
            if (ManufactureOrderType.MANUFACTURE_ORDER_BY_STANDARD.name().equals(this.getManufactureOrderType())) {
                log.info("Sort by order index");
                list.sort(Comparator.comparing(WorkOrderDTO::getDailyIndex));
            } else {
                list.sort(Comparator.comparing(WorkOrderDTO::getOrderIndex));
            }
            dto.setWorkOrders(list);
        }
        return dto;
    }

    public ManuFactureDTO toDtoManuFactureDTO() {
        ManuFactureDTO dto = new ManuFactureDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setCode(this.code);
        dto.setFromDate(this.getFromDate());
        dto.setStatus(this.getStatus());
        dto.setToDate(this.getToDate());
        dto.setToDate(this.getToDate());
        dto.setProductionQuantity(this.productionQuantity);
        dto.setAttributes(this.attributes);
        dto.setOrderId(this.getOrderId());
        dto.setProductionStandardId(this.productionStandardId);
        dto.setCreatedAt(this.getCreatedAt());
        dto.setCreatedBy(this.getCreatedBy());
        dto.setItemId(this.materialId);
        dto.setTypePage(this.manufactureOrderType);
        dto.setLastUpdated(this.getLastUpdated());
        dto.setOrderItemId(this.orderItemId);


        System.out.println("qualityCheckSample: " + qualityCheckSample);

        if (qualityCheckSample != null) {
            if (dto.getQualityCheckSampleDTO() == null) {
                dto.setQualityCheckSampleDTO(new QualityCheckSampleDTO());
            }
            dto.setQualityCheckSampleDTO(qualityCheckSample.toDto());

            if (qualityCheckSample.getDisposal() != null) {
                dto.getQualityCheckSampleDTO().setDisposal(qualityCheckSample.getDisposal().toDto());
            }
        }
        if (productPackage != null) {
            if (dto.getProductPackageDTO() == null) {
                dto.setProductPackageDTO(new ProductPackageDTO());
            }
            dto.setProductPackageDTO(productPackage.toDto());
        }
        if (productMaintain != null) {
            if (dto.getProductMaintainDTO() == null) {
                dto.setProductMaintainDTO(new ProductMaintainDTO());
            }
            dto.setProductMaintainDTO(productMaintain.toDto());
        }
        if (productionRouting != null) {
            if (dto.getProductRoutingDTO() == null) {
                dto.setProductRoutingDTO(new ProductRoutingDTO());
            }
            dto.setProductRoutingDTO(productionRouting.toDto());
        }
        return dto;

    }

    public void toUpdate(ManuFactureDTO manuFactureDTO) {
        this.setId(manuFactureDTO.getId());
        this.setName(manuFactureDTO.getName());
        this.setCode(manuFactureDTO.getCode());
        this.setFromDate(manuFactureDTO.getFromDate());
        this.setToDate(manuFactureDTO.getToDate());
//        this.setAttributes(manuFactureDTO.getAttributes());
        this.setOrderId(manuFactureDTO.getOrderId());
        this.setProductionQuantity(manuFactureDTO.getProductionQuantity());
        this.setProductionStandardId(manuFactureDTO.getProductionStandardId());

    }
}
