package com.masi.employee.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.employee.domain.Employee;
import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.enumeration.*;
import com.masi.employee.helper.ExcelCell;
import com.masi.employee.helper.SheetTitle;
import io.r2dbc.postgresql.codec.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.EmployeeProfile} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
@NoArgsConstructor
@SheetTitle("Báo cáo theo dõi nhân viên sắp hết hạn hợp đồng")
public class EmployeeProfileDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "must not be null")
    @ExcelCell(name = "Mã nhân viên", column = 0)
    private String employeeCode;

    private Boolean isHasProfileAttachment;

    @NotNull(message = "must not be null")
    @ExcelCell(name = "Họ và Tên", column = 1)
    private String fullName;

    public String getFullName() {
        if (this.fullName == null) {
            return "";
        }
        return this.fullName;
    }

    @NotNull(message = "must not be null")
    private Gender gender;

    @NotNull(message = "must not be null")
    private UUID workspaceId;

    @NotNull(message = "must not be null")
    private String citizenId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProfileAttachmentDTO> files;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = ConfirmLeaveDTO.class)
    private ConfirmLeaveDTO confirmLeave;

    private LocalDate citizenIssueDate;

    private String citizenIssuePlace;

    private String residenceAddress;

    private String temporaryAddress;

    private LocalDate birthday;

    private String phone;

    private String taxCode = "";

    @NotNull(message = "must not be null")
    private LocalDate startWorkDate;

    @NotNull(message = "must not be null")
    private String role;

    @NotNull(message = "must not be null")
    private Position position;

    private String bankCode;

    private String bankNumber;

    @NotNull(message = "must not be null")
    private ContractType contractType;


    private String contractTerm = "Không xác định";

    @NotNull(message = "must not be null")
    @ExcelCell(name = "Số hợp đồng", column = 2)
    private String contractNumber;

    @NotNull(message = "must not be null")
    private LocalDate contractDate;


    @ExcelCell(name = "Ngày kết thúc hợp đồng", column = 3)
    private LocalDate contractEndDate;

    private String level;

    private String parkingCard;

    private String insuranceCard;

    private UUID referrerId;

    private LocalDate referrerDate;

    private String email;

    private String note;

    private EmployeeStatus status = EmployeeStatus.WORKING;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WorkspaceDTO workspace;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO referrer;

    private LocalDate probationDateFrom;

    private LocalDate probationDateTo;

    private String officialWorkType;

    private Float officialWorkTypeDuration;

    private Float insurancePaymentLevel;

    private Float numberDaysOff = 12f;

    private Float useDaysOff = 0f;

    private String userName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String pin;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String accountStatus;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json companyJson;

    private Boolean isActivated;

    public Long getOfficialWorkTypeDurationYear() {
        if (this.startWorkDate == null) {
            return 0L;
        }
        var now = LocalDate.now();
        return ChronoUnit.YEARS.between(this.startWorkDate, now);
    }


    public EmployeeProfileDTO(UUID id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public ProfileState getProfileState() {
        if (Boolean.TRUE.equals(this.isActive)) {
            return ProfileState.ACTIVE;
        }
        return ProfileState.INACTIVE;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeProfileDTO)) {
            return false;
        }

        EmployeeProfileDTO employeeProfileDTO = (EmployeeProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public void applyUpdateTo(EmployeeProfile employeeProfile) {
        employeeProfile.setFullName(this.fullName);
        employeeProfile.setGender(this.gender);
        employeeProfile.setWorkspaceId(this.workspaceId);
        employeeProfile.setCitizenId(this.citizenId);
        employeeProfile.setCitizenIssueDate(this.citizenIssueDate);
        employeeProfile.setCitizenIssuePlace(this.citizenIssuePlace);
        employeeProfile.setResidenceAddress(this.residenceAddress);
        employeeProfile.setTemporaryAddress(this.temporaryAddress);
        employeeProfile.setBirthday(this.birthday);
        employeeProfile.setPhone(this.phone);
        employeeProfile.setTaxCode(this.taxCode);
        employeeProfile.setStartWorkDate(this.startWorkDate);
        employeeProfile.setRole(this.role);
        employeeProfile.setPosition(this.position);
        employeeProfile.setBankCode(this.bankCode);
        employeeProfile.setBankNumber(this.bankNumber);
        employeeProfile.setContractType(this.contractType);
        employeeProfile.setContractTerm(this.contractTerm);
        employeeProfile.setContractNumber(this.contractNumber);
        employeeProfile.setContractDate(this.contractDate);
        employeeProfile.setContractEndDate(this.contractEndDate);
        employeeProfile.setLevel(this.level);
        employeeProfile.setParkingCard(this.parkingCard);
        employeeProfile.setInsuranceCard(this.insuranceCard);
        employeeProfile.setReferrerId(this.referrerId);
        employeeProfile.setReferrerDate(this.referrerDate);
        employeeProfile.setEmail(this.email);
        employeeProfile.setNote(this.note);
        employeeProfile.setStatus(this.status);
        employeeProfile.setUpdatedAt(ZonedDateTime.now());
        employeeProfile.setProbationDateFrom(this.probationDateFrom);
        employeeProfile.setProbationDateTo(this.probationDateTo);
        employeeProfile.setOfficialWorkType(this.officialWorkType);
        employeeProfile.setOfficialWorkTypeDuration(this.officialWorkTypeDuration);
        employeeProfile.setInsurancePaymentLevel(this.insurancePaymentLevel);
        employeeProfile.setPin(this.pin);

    }

    public EmployeeProfile toEntity() {
        EmployeeProfile employeeProfile = new EmployeeProfile();
        employeeProfile.setId(this.id);
        employeeProfile.setEmployeeCode(this.employeeCode);
        employeeProfile.setFullName(this.fullName);
        employeeProfile.setGender(this.gender);
        employeeProfile.setWorkspaceId(this.workspaceId);
        employeeProfile.setCitizenId(this.citizenId);
        employeeProfile.setCitizenIssueDate(this.citizenIssueDate);
        employeeProfile.setCitizenIssuePlace(this.citizenIssuePlace);
        employeeProfile.setResidenceAddress(this.residenceAddress);
        employeeProfile.setTemporaryAddress(this.temporaryAddress);
        employeeProfile.setBirthday(this.birthday);
        employeeProfile.setPhone(this.phone);
        employeeProfile.setTaxCode(this.taxCode);
        employeeProfile.setStartWorkDate(this.startWorkDate);
        employeeProfile.setRole(this.role);
        employeeProfile.setPosition(this.position);
        employeeProfile.setBankCode(this.bankCode);
        employeeProfile.setBankNumber(this.bankNumber);
        employeeProfile.setContractType(this.contractType);
        employeeProfile.setContractTerm(this.contractTerm);
        employeeProfile.setContractNumber(this.contractNumber);
        employeeProfile.setContractDate(this.contractDate);
        employeeProfile.setContractEndDate(this.contractEndDate);
        employeeProfile.setLevel(this.level);
        employeeProfile.setParkingCard(this.parkingCard);
        employeeProfile.setInsuranceCard(this.insuranceCard);
        employeeProfile.setReferrerId(this.referrerId);
        employeeProfile.setReferrerDate(this.referrerDate);
        employeeProfile.setEmail(this.email);
        employeeProfile.setNote(this.note);
        employeeProfile.setStatus(this.status);
        employeeProfile.setCreatedAt(ZonedDateTime.now());
        employeeProfile.setUpdatedAt(ZonedDateTime.now());
        employeeProfile.setIsDeleted(false);
        employeeProfile.setIsActive(true);
        employeeProfile.setProbationDateFrom(this.probationDateFrom);
        employeeProfile.setProbationDateTo(this.probationDateTo);
        employeeProfile.setOfficialWorkType(this.officialWorkType);
        employeeProfile.setOfficialWorkTypeDuration(this.officialWorkTypeDuration);
        employeeProfile.setInsurancePaymentLevel(this.insurancePaymentLevel);
        employeeProfile.setPin(this.pin);
        employeeProfile.setAccountStatus(EmployeeAccountStatus.NOT_HAVING_ACCOUNT.name());
        return employeeProfile;

    }

}
