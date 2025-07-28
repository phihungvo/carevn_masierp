package com.masi.production.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.production.domain.QualityCheckSample;
import com.masi.production.domain.enumeration.QcSampleStatus;
import io.r2dbc.postgresql.codec.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.QualityCheckSample} entity.
 */@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QualityCheckSampleDTO implements Serializable {

    private UUID id;

    private LocalDate samplingDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedSamplingDate;

    private String sampleNo;

    private String productType;

    private Float sampleWeight;

    private String customer;

    private String reason;

    private LocalDate sampleReleaseDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedSampleReleaseDate;

    private String internalHum;

    private String internalTvn;

    private String internalAsh;

    private String internalProtein;

    private String externalHum;

    private String externalTvn;

    private String externalAsh;

    private String externalProtein;

    private UUID samplingEmployeeId;

    private UUID itemId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private QcSampleStatus status;

    private String note;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = SampleDisposalDTO.class)
    private SampleDisposalDTO disposal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID disposalId;

    private UUID manufactureOrderId;

    private ManufactureOrderDTO manufactureOrder;

    private UUID packageId;

    private ProductPackageDTO productPackage;

    private Float proteinPercentageApply;

    private Boolean isDeleted;

    private String deletedBy;

    private ZonedDateTime deletedAt;

    private Boolean isDone;


    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attributes;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QualityCheckSampleDTO)) {
            return false;
        }

        QualityCheckSampleDTO qualityCheckSampleDTO = (QualityCheckSampleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, qualityCheckSampleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QualityCheckSampleDTO{" +
            "id='" + getId() + "'" +
            ", samplingDate='" + getSamplingDate() + "'" +
            ", sampleNo='" + getSampleNo() + "'" +
            ", productType='" + getProductType() + "'" +
            ", sampleWeight=" + getSampleWeight() +
            ", customer='" + getCustomer() + "'" +
            ", reason='" + getReason() + "'" +
            ", sampleReleaseDate='" + getSampleReleaseDate() + "'" +
            ", internalHum='" + getInternalHum() + "'" +
            ", internalTvn='" + getInternalTvn() + "'" +
            ", internalAsh='" + getInternalAsh() + "'" +
            ", internalProtein='" + getInternalProtein() + "'" +
            ", externalHum='" + getExternalHum() + "'" +
            ", externalTvn='" + getExternalTvn() + "'" +
            ", externalAsh='" + getExternalAsh() + "'" +
            ", externalProtein='" + getExternalProtein() + "'" +
            ", samplingEmployeeId='" + getSamplingEmployeeId() + "'" +
            ", status='" + getStatus() + "'" +
            ", note='" + getNote() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", disposal=" + getDisposal() +
            "}";
    }


}
