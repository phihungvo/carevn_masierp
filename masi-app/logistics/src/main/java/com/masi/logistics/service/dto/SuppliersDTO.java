package com.masi.logistics.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.SupplierType;
import com.masi.logistics.service.exportDTO.SupplierExportDTO;
import io.r2dbc.postgresql.codec.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.Suppliers} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuppliersDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code;

    private ZonedDateTime birthday;

    private String name;

    private String email;

    private String address;

    private String addressService;

    private String phone;

    private String bankInfo;

    private String taxCode;

    private String contact;

    private ZonedDateTime paymentTerm;

    private Integer paymentTermNumber;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SupplierTypeDTO supplierType;

    private UUID supplierTypeId;

    @Lob
    private String paymentTermText;

    private String fax;

    private String shortName;

    private String note;

    private Boolean isActive;

    private String fullName;

    private String position;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updateAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deleteAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deleteBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {SupplierGroupDTO.class})
    private SupplierGroupDTO supplierGroup;

    private UUID supplierGroupId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Collection<SupplierDetailDTO> suppliesDetails;

    private Collection<ContactDTO> contacts;

    public Collection<ContactGiftDTO> listContactGiftsInContact() {
        return contacts.stream().map(ContactDTO::getContactGifts).filter(x -> !Objects.isNull(x) && !x.isEmpty()).flatMap(Collection::stream).toList();
    }

    private Collection<SupplierContractDTO> supplierContracts;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json debtEmployees;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attachment;

    private UUID managerId;

    public SupplierExportDTO toExportDTO() {
        var paymentTermFormat = "dd/MM/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(paymentTermFormat);
        String formattedPaymentTerm = paymentTerm != null ? paymentTerm.format(formatter) : "";

        return new SupplierExportDTO(code, name, address, taxCode, fullName, phone, email, formattedPaymentTerm, isActive ? "Hoạt động" : "Ngừng hoạt động");
    }

}
