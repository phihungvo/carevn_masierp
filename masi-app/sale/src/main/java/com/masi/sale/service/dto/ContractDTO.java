package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.sale.domain.Contract;
import com.masi.sale.domain.enumeration.ContractStatus;
import com.masi.sale.domain.enumeration.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A DTO for the {@link com.masi.sale.domain.Contract} entity.
 */
@Setter
@Getter
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractDTO implements Serializable, Reviewable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    private ContractStatus oldStatus;
    @NotNull(message = "must not be null")
    private ContractStatus status;

    @NotNull(message = "must not be null")
    private String contractName;

    @NotNull(message = "must not be null")
    private LocalDate contractValidFrom;

    @NotNull(message = "must not be null")
    private LocalDate contractValidTo;

    private UUID customerId;

    @NotNull(message = "must not be null")
    private ContractType contractType;

    @NotNull(message = "must not be null")
    private UUID contractOwner;

    @NotNull(message = "must not be null")
    private BigDecimal contractTotal;

    //    @NotNull(message = "must not be null")
    private String proteinPercent = "";

    private LocalDate deliveryTermFrom;

    private LocalDate deliveryTermTo;

    private String payTerm;

    private String payCondition;

    private String deliveryLocation;

    private String monetaryUnit;

    private float exchangeRate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CustomerDTO customer;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String approvalSignFile;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String approvalSignFileName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = FileAttachmentDTO.class)
    private FileAttachmentDTO approvalSignFileAttachment;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String rejectNote;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ContractFile contractFile;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<FileAttachmentDTO> fileAttachments;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "owner")
    private EmployeeDTO employee;

    private Collection<ContractMaterialDTO> contractMaterialDTOS;

    private Collection<ContractProductDTO> contractProductDTOS;

    private ZonedDateTime reviewAt;

    private UUID reviewBy;
    private List<RequestApprovalDTO> requestApprovals;
    private List<RequestApprovalDTO> normalApprovals;

    private UUID quotationId;


    public UUID getCustomerId() {
        if (customerId == null) return UUID.randomUUID();
        return customerId;
    }

//    public ContractFile getContractFile() {
//        return contractFile != null ? contractFile : new ContractFile("", "");
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractDTO)) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "ContractDTO{" + "id=" + id + ", status=" + status + ", contractName='" + contractName + '\'' + ", contractType=" + contractType + ", contractOwner=" + contractOwner + ", contractTotal=" + contractTotal + ", proteinPercent='" + proteinPercent + '\'' + ", rejectNote='" + rejectNote + '\'' + ", lastUpdated=" + lastUpdated + ", createdDate=" + createdDate + ", isDeleted=" + isDeleted + ", base64Request=" + contractFile + '}';
    }

    public void applyUpdate(Contract contract) {
        contract.setId(this.id);
        contract.setStatus(this.status);
        contract.setContractName(this.contractName);
        contract.setContractValidTo(this.contractValidTo);
        contract.setContractValidFrom(this.contractValidFrom);
        contract.setCustomerId(this.customerId);
        contract.setContractType(this.contractType);
        contract.setContractOwner(this.contractOwner);
        contract.setContractTotal(this.contractTotal);
        contract.setProteinPercent(this.proteinPercent);
        contract.setApprovalSignFile(this.approvalSignFile);
        contract.setRejectNote(this.rejectNote);

        contract.setDeliveryTermFrom(this.deliveryTermFrom);
        contract.setDeliveryTermTo(this.deliveryTermTo);
        contract.setPayTerm(this.payTerm);
        contract.setPayCondition(this.payCondition);
        contract.setDeliveryLocation(this.deliveryLocation);
        contract.setMonetaryUnit(this.monetaryUnit);
        contract.setExchangeRate(this.exchangeRate);
        contract.setQuotationId(this.quotationId);

    }

    @Override
    public UUID getDocumentId() {
        return id;
    }

    @Override
    public void addReview(RequestApprovalDTO review) {
        if (requestApprovals == null) {
            requestApprovals = new ArrayList<>();
            requestApprovals.add(review);
        } else {
            requestApprovals.add(review);
        }
    }
    public void addNormalReview(RequestApprovalDTO review) {
        if (normalApprovals == null) {
            normalApprovals = new ArrayList<>();
            normalApprovals.add(review);
        } else {
            normalApprovals.add(review);
        }
    }
}
