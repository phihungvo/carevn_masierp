package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.domain.enumeration.QcSampleStatus;
import com.masi.production.service.dto.QualityCheckSampleDTO;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A QualityCheckSample.
 */
@Data
@Table("quality_check_sample")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QualityCheckSample implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("sampling_date")
    private LocalDate samplingDate;

    @Column("sample_no")
    private String sampleNo;

    @Column("product_type")
    private String productType;

    @Column("sample_weight")
    private Float sampleWeight;

    @Column("customer")
    private String customer;

    @Column("reason")
    private String reason;

    @Column("sample_release_date")
    private LocalDate sampleReleaseDate;

    @Column("internal_hum")
    private String internalHum;

    @Column("internal_tvn")
    private String internalTvn;

    @Column("internal_ash")
    private String internalAsh;

    @Column("internal_protein")
    private String internalProtein;

    @Column("external_hum")
    private String externalHum;

    @Column("external_tvn")
    private String externalTvn;

    @Column("external_ash")
    private String externalAsh;

    @Column("external_protein")
    private String externalProtein;

    @Column("sampling_employee_id")
    private UUID samplingEmployeeId;

    @Column("status")
    private QcSampleStatus status;

    @Column("item_id")
    private UUID itemId;

    @Column("note")
    private String note;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Transient
    private boolean isPersisted;

    @Transient
    private SampleDisposal disposal;

    @Column("disposal_id")
    private UUID disposalId;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Column("manufacture_order_id")
    private UUID manufactureOrderId;

    @Column("package_id")
    private UUID packageId;

    @Transient
    @JsonIgnoreProperties(value = { "qualityCheckSamples"}, allowSetters = true)
    private ProductPackage productPackage;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private UUID deletedBy;

    @Column("protein_percentage_apply")
    private Float proteinPercentageApply;

    @Column("attributes")
    private Json attributes;

    @Transient
    private ManufactureOrder manufactureOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public QualityCheckSample setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public QualityCheckSample disposal(SampleDisposal sampleDisposal) {
        this.setDisposal(sampleDisposal);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public QualityCheckSampleDTO toDto() {
        QualityCheckSampleDTO dto = new QualityCheckSampleDTO();
        dto.setId(this.id);
        dto.setSamplingDate(this.samplingDate);
        if (Objects.nonNull(this.samplingDate)) dto.setZonedSamplingDate(this.samplingDate.atStartOfDay().atZone(ZoneOffset.UTC));
        dto.setSampleNo(this.sampleNo);
        dto.setProductType(this.productType);
        dto.setSampleWeight(this.sampleWeight);
        dto.setCustomer(this.customer);
        dto.setReason(this.reason);
        dto.setSampleReleaseDate(this.sampleReleaseDate);
        if (Objects.nonNull(this.sampleReleaseDate)) dto.setZonedSampleReleaseDate(this.sampleReleaseDate.atStartOfDay().atZone(ZoneOffset.UTC));
        dto.setInternalHum(this.internalHum);
        dto.setInternalTvn(this.internalTvn);
        dto.setInternalAsh(this.internalAsh);
        dto.setInternalProtein(this.internalProtein);
        dto.setExternalHum(this.externalHum);
        dto.setExternalTvn(this.externalTvn);
        dto.setExternalAsh(this.externalAsh);
        dto.setExternalProtein(this.externalProtein);
        dto.setSamplingEmployeeId(this.samplingEmployeeId);
        dto.setStatus(this.status);
        dto.setNote(this.note);
        dto.setCreatedAt(this.createdAt);
        dto.setLastUpdated(this.lastUpdated);
        dto.setAttributes(this.attributes);
        dto.setIsActive(this.isActive);
        dto.setProteinPercentageApply(this.proteinPercentageApply);
        dto.setPackageId(this.packageId);
        dto.setManufactureOrderId(this.manufactureOrderId);
        if(this.manufactureOrder != null) {
            dto.setManufactureOrder(this.manufactureOrder.toDto());
        }
        if(this.disposal != null) {
            dto.setDisposal(this.disposal.toDto());
        }
        if(this.productPackage != null) {
            dto.setProductPackage(this.productPackage.toDto());
        }
        dto.setIsDeleted(this.isDeleted);
        dto.setDeletedAt(this.deletedAt);
        dto.setDeletedBy(this.deletedBy != null ? this.deletedBy.toString() : null);
        dto.setProteinPercentageApply(this.proteinPercentageApply);
        dto.setItemId(this.itemId);
        return dto;
    }

    public boolean areAllFieldsNonNull() {
        return
                samplingDate != null &&
                        sampleNo != null &&
                        productType != null &&
                        sampleWeight != null &&
                        customer != null &&
                        internalHum != null &&
                        internalTvn != null &&
                        internalAsh != null &&
                        internalProtein != null &&
                        externalHum != null &&
                        externalTvn != null &&
                        externalAsh != null &&
                        externalProtein != null &&
                        samplingEmployeeId != null &&
                        manufactureOrderId != null;
    }
}
