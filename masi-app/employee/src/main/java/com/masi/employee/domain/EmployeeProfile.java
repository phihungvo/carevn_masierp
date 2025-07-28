package com.masi.employee.domain;

import com.masi.employee.domain.enumeration.*;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A EmployeeProfile.
 */
@Data
@Table("employee_profile")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeProfile implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;


    @NotNull(message = "must not be null")
    @Column("employee_code")
    private String employeeCode;

    @Transient
    private boolean isHasProfileAttachment = false;

    @NotNull(message = "must not be null")
    @Column("full_name")
    private String fullName;

    @NotNull(message = "must not be null")
    @Column("gender")
    private Gender gender;

    @Transient
    private Employee employee;

    @NotNull(message = "must not be null")
    @Column("workspace_id")
    private UUID workspaceId;

    @NotNull(message = "must not be null")
    @Column("citizen_id")
    private String citizenId;

    @Column("citizen_issue_date")
    private LocalDate citizenIssueDate;

    @Column("citizen_issue_place")
    private String citizenIssuePlace;

    @Column("residence_address")
    private String residenceAddress;

    @Column("temporary_address")
    private String temporaryAddress;

    @Column("birthday")
    private LocalDate birthday;

    @Column("phone")
    private String phone;

    @Column("tax_code")
    @Builder.Default
    private String taxCode = "";

    @NotNull(message = "must not be null")
    @Column("start_work_date")
    private LocalDate startWorkDate;

    @NotNull(message = "must not be null")
    @Column("role")
    private String role;

    @NotNull(message = "must not be null")
    @Column("position")
    private Position position;

    @Column("bank_code")
    private String bankCode;

    @Column("bank_number")
    private String bankNumber;

    @NotNull(message = "must not be null")
    @Column("contract_type")
    private ContractType contractType;


    @NotNull(message = "must not be null")
    @Column("contract_term")
    private String contractTerm = "Không xác định"; // default value

    @NotNull(message = "must not be null")
    @Column("contract_number")
    private String contractNumber;

    public String getContractNumber() {
        if (contractNumber == null || contractNumber.isEmpty()) {
            return employeeCode;
        }
        return contractNumber;
    }

    @Column("contract_date")
    private LocalDate contractDate;

    @Column("account_status")
    private String accountStatus = EmployeeAccountStatus.NOT_HAVING_ACCOUNT.name();

    public String getAccountStatus() {
        if (accountStatus == null || accountStatus.isEmpty()) {
            return EmployeeAccountStatus.NOT_HAVING_ACCOUNT.name();
        }
        return accountStatus;
    }

    public void setContractDate(LocalDate contractDate) {
        if (contractDate == null) {
            this.contractDate = LocalDate.now();
            return;
        }
        this.contractDate = contractDate;
    }

    public LocalDate getContractDate() {
        if (contractDate == null) {
            return LocalDate.now();
        }
        return contractDate;
    }

    @Column("contract_end_date")
    private LocalDate contractEndDate;

    @Column("level")
    private String level;

    @Column("parking_card")
    private String parkingCard;

    @Column("insurance_card")
    private String insuranceCard;

    @Column("referrer_id")
    private UUID referrerId;

    @Column("referrer_date")
    private LocalDate referrerDate;

    @Column("email")
    private String email;

    @Column("note")
    private String note;

    @Column("status")
    private EmployeeStatus status;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive = true; // default value

    @Transient
    private boolean isPersisted;

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    @Transient
    private Workspace workspace;

    @Transient
    private Employee referrer;

    @Column("company")
    private String company;

    @Column("department")
    private String department;


    @Column("probation_date_from")
    private LocalDate probationDateFrom;

    @Column("probation_date_to")
    private LocalDate probationDateTo;

    @Column("official_work_type")
    private String officialWorkType;

    @Column("official_work_type_duration")
    private Float officialWorkTypeDuration;

    @Column("insurance_payment_level")
    private Float insurancePaymentLevel;

    @Column("pin")
    private String pin;

    public EmployeeProfile setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public String getFirstName() {
        var split = this.fullName.trim().replaceAll("\\s+", " ").split(" ");
        if (split.length == 0) {
            return "";
        }
        return split[split.length - 1];
    }

    public String getLastName() {
        var split = this.fullName.trim().replaceAll("\\s+", " ").split(" ");
        if (split.length == 0) {
            return "";
        }
        StringBuilder lastName = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            lastName.append(split[i]);
            if (i < split.length - 2) {
                lastName.append(" ");
            }
        }
        return lastName.toString();
    }
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmployeeProfile id(UUID id) {
        this.setId(id);
        return this;
    }


    public EmployeeProfile employeeCode(String employeeCode) {
        this.setEmployeeCode(employeeCode);
        return this;
    }

    public EmployeeProfile fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public EmployeeProfile gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public EmployeeProfile workspaceId(UUID workspaceId) {
        this.setWorkspaceId(workspaceId);
        return this;
    }


    @Override
    public UUID getId() {
        return id;
    }

    public EmployeeProfile citizenId(String citizenId) {
        this.setCitizenId(citizenId);
        return this;
    }

    public EmployeeProfile citizenIssueDate(LocalDate citizenIssueDate) {
        this.setCitizenIssueDate(citizenIssueDate);
        return this;
    }

    public EmployeeProfile citizenIssuePlace(String citizenIssuePlace) {
        this.setCitizenIssuePlace(citizenIssuePlace);
        return this;
    }

    public EmployeeProfile residenceAddress(String residenceAddress) {
        this.setResidenceAddress(residenceAddress);
        return this;
    }

    public EmployeeProfile temporaryAddress(String temporaryAddress) {
        this.setTemporaryAddress(temporaryAddress);
        return this;
    }

    public EmployeeProfile birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public EmployeeProfile phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public EmployeeProfile taxCode(String taxCode) {
        this.setTaxCode(taxCode);
        return this;
    }

    public EmployeeProfile startWorkDate(LocalDate startWorkDate) {
        this.setStartWorkDate(startWorkDate);
        return this;
    }

    public EmployeeProfile role(String role) {
        this.setRole(role);
        return this;
    }

    public EmployeeProfile position(Position position) {
        this.setPosition(position);
        return this;
    }

    public EmployeeProfile bankCode(String bankCode) {
        this.setBankCode(bankCode);
        return this;
    }

    public EmployeeProfile bankNumber(String bankNumber) {
        this.setBankNumber(bankNumber);
        return this;
    }

    public EmployeeProfile contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }

    public EmployeeProfile contractTerm(String contractTerm) {
        this.setContractTerm(contractTerm);
        return this;
    }

    public EmployeeProfile contractNumber(String contractNumber) {
        this.setContractNumber(contractNumber);
        return this;
    }

    public EmployeeProfile contractDate(LocalDate contractDate) {
        this.setContractDate(contractDate);
        return this;
    }

    public EmployeeProfile contractEndDate(LocalDate contractEndDate) {
        this.setContractEndDate(contractEndDate);
        return this;
    }

    public EmployeeProfile level(String level) {
        this.setLevel(level);
        return this;
    }

    public EmployeeProfile parkingCard(String parkingCard) {
        this.setParkingCard(parkingCard);
        return this;
    }

    public EmployeeProfile insuranceCard(String insuranceCard) {
        this.setInsuranceCard(insuranceCard);
        return this;
    }

    public EmployeeProfile referrerId(UUID referrerId) {
        this.setReferrerId(referrerId);
        return this;
    }

    public EmployeeProfile referrerDate(LocalDate referrerDate) {
        this.setReferrerDate(referrerDate);
        return this;
    }

    public EmployeeProfile email(String email) {
        this.setEmail(email);
        return this;
    }

    public EmployeeProfile note(String note) {
        this.setNote(note);
        return this;
    }

    public EmployeeProfile status(EmployeeStatus status) {
        this.setStatus(status);
        return this;
    }

    public EmployeeProfile createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public EmployeeProfile updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public EmployeeProfile isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public EmployeeProfile isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public Long getOfficialWorkTypeDurationYear() {
        if (this.workspace != null && workspace.getWorkspaceType() == WorkspaceType.FACTORY) {
            return 0L;
        }
        if (this.startWorkDate == null) {
            return 0L;
        }
        var now = LocalDate.now();
        return ChronoUnit.YEARS.between(this.startWorkDate, now);
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((EmployeeProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public EmployeeProfileDTO toBriefDTO() {
        EmployeeProfileDTO dto = new EmployeeProfileDTO();
        dto.setId(this.getId());
        dto.setEmployeeCode(this.getEmployeeCode());
        dto.setFullName(this.getFullName());
        dto.setGender(this.getGender());
        dto.setRole(this.getRole());
        dto.setPosition(this.getPosition());
        dto.setStatus(this.getStatus());
        dto.setIsActive(this.getIsActive());
        dto.setContractEndDate(this.getContractEndDate());
        dto.setContractNumber(this.getContractNumber());
        dto.setContractDate(this.getContractDate());
        dto.setWorkspace(null == this.workspace ? null : this.workspace.toDTO());
        dto.setStartWorkDate(this.getStartWorkDate());
        dto.setContractTerm(this.getContractTerm());
        dto.setPin(this.getPin());
        return dto;
    }

    public EmployeeProfileDTO toDTO() {
        EmployeeProfileDTO dto = this.toBriefDTO();
        dto.setWorkspaceId(this.getWorkspaceId());
        dto.setCitizenId(this.getCitizenId());
        dto.setCitizenIssueDate(this.getCitizenIssueDate());
        dto.setCitizenIssuePlace(this.getCitizenIssuePlace());
        dto.setResidenceAddress(this.getResidenceAddress());
        dto.setTemporaryAddress(this.getTemporaryAddress());
        dto.setBirthday(this.getBirthday());
        dto.setPhone(this.getPhone());
        dto.setTaxCode(this.getTaxCode());
        dto.setBankCode(this.getBankCode());
        dto.setBankNumber(this.getBankNumber());
        dto.setContractType(this.getContractType());
        dto.setContractTerm(this.getContractTerm());
        dto.setLevel(this.getLevel());
        dto.setParkingCard(this.getParkingCard());
        dto.setInsuranceCard(this.getInsuranceCard());
        dto.setReferrerId(this.getReferrerId());
        dto.setReferrerDate(this.getReferrerDate());
        dto.setEmail(this.getEmail());
        dto.setNote(this.getNote());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setUpdatedAt(this.getUpdatedAt());
        dto.setIsDeleted(this.getIsDeleted());
        dto.setReferrer(null == this.referrer ? null : this.referrer.toDto());
        dto.setProbationDateFrom(this.getProbationDateFrom());
        dto.setProbationDateTo(this.getProbationDateTo());
        dto.setOfficialWorkType(this.getOfficialWorkType());
        dto.setOfficialWorkTypeDuration(this.getOfficialWorkTypeDuration());
        dto.setInsurancePaymentLevel(this.getInsurancePaymentLevel());
        dto.setPin(this.getPin());
        dto.setAccountStatus(this.getAccountStatus());
        return dto;
    }

    public Employee toShortEmployee() {
        Employee employee = new Employee();
        employee.setEmployeeProfile(this);
        employee.setId(this.getId());
        employee.setFullName(this.getFullName());
        employee.setLastName(this.getLastName());
        employee.setFirstName(this.getFirstName());
        employee.setWorkspace(this.getWorkspace());
        employee.setWorkspaceId(this.getWorkspaceId());
        return employee;
    }
}
