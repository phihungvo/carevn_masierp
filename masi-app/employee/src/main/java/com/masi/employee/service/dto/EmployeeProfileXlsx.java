package com.masi.employee.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.Workspace;
import com.masi.employee.domain.enumeration.ContractType;
import com.masi.employee.domain.enumeration.EmployeeStatus;
import com.masi.employee.domain.enumeration.Gender;
import com.masi.employee.domain.enumeration.Position;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class EmployeeProfileXlsx {
    private UUID id;
    private String employeeCode;
    private boolean isIgnore = false;
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;
    @NotNull(message = "Giới tính không được để trống")
    private Gender gender;
    @NotBlank (message = "Tên phòng ban không được để trống")
    private String workspaceName;
    private UUID workspaceId;
    private String citizenId="";
    private LocalDate citizenIssueDate;
    private String citizenIssuePlace;
    private String residenceAddress;
    private String temporaryAddress;
    private LocalDate birthday;
    //|^$
    @Pattern(regexp = "^(84|0[3|5|7|8|9])[0-9]{8}$", message = "Số điện thoại không hợp lệ")
    private String phone;
  //  @NotBlank(message = "Mã số thuế không được để trống")
    private String taxCode;
    @NotNull(message = "Ngày bắt đầu làm việc không được để trống")
    private LocalDate startWorkDate;
    @NotBlank(message = "Chức vụ không được để trống")
    private String role;
    @NotNull(message = "Vị trí không được để trống")
    private Position position;
    private String bankCode;
    private String bankNumber;
    @NotNull(message = "Loại hợp đồng không được để trống")
    private ContractType contractType;
    private String contractTerm;
    @NotBlank(message = "Số hợp đồng không được để trống")
    private String contractNumber;
    @NotNull(message = "Ngày ký hợp đồng không được để trống")
    private LocalDate contractDate;
    private LocalDate contractEndDate;
    private String level;
    private String parkingCard;
    private String insuranceCard;
    private String referrerCode;
    private UUID referrerId;
    private LocalDate referrerDate;
    //    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$|^$", message = "Email không hợp lệ")
    @Email(message = "Email không hợp lệ")
    private String email;
    private String note;
    private EmployeeStatus status;
    private String company = "KIM_LONG";
    private LocalDate probationDateFrom;
    private LocalDate probationDateTo;
    private String officialWorkType;
    private Float officialWorkTypeDuration;
    private Float insurancePaymentLevel;

    private String error = "";
    private int rowNumber;


    public boolean isHasExistedCode() {
        // số không trùng với định dạng thì trả về true
        var employeeCodeRegex = "^[FM]\\d{5}$";
        return StringUtils.isNotBlank(employeeCode) && !employeeCode.matches(employeeCodeRegex);
    }

    public boolean isMaybeExistedCode() {
        // trùng với định dạng thì trả về true
        var employeeCodeRegex = "^[FM]\\d{5}$";
        return StringUtils.isNotBlank(employeeCode) && employeeCode.matches(employeeCodeRegex);
    }

    public boolean isNeedNewCode() {
        // nếu mã nhân viên rỗng hoặc không trùng với định dạng thì trả về true
        var employeeCodeRegex = "^[FM]\\d{5}$";
        return StringUtils.isBlank(employeeCode) || employeeCode.matches(employeeCodeRegex);
    }

    // this code is suck, so am i
    public EmployeeProfile toEntity() {
        // Create a new EmployeeProfile object
        var employeeProfile = new EmployeeProfile();

        // Set all the properties from this class to the EmployeeProfile object
        employeeProfile.setId(UUID.randomUUID());
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
        employeeProfile.setReferrerId(this.referrerId);
        employeeProfile.setReferrerDate(this.referrerDate);
        employeeProfile.setEmail(this.email);
        employeeProfile.setNote(this.note);
        employeeProfile.setStatus(this.status);
        employeeProfile.setCompany(this.company);
        employeeProfile.setProbationDateFrom(this.probationDateFrom);
        employeeProfile.setProbationDateTo(this.probationDateTo);
        employeeProfile.setOfficialWorkType(this.officialWorkType);
        employeeProfile.setOfficialWorkTypeDuration(this.officialWorkTypeDuration);
        employeeProfile.setInsurancePaymentLevel(this.insurancePaymentLevel);
        employeeProfile.setCreatedAt(ZonedDateTime.now());
        employeeProfile.setUpdatedAt(ZonedDateTime.now());
        employeeProfile.setIsDeleted(false);
        employeeProfile.setIsActive(true);

        // Return the populated EmployeeProfile object
        return employeeProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeProfileXlsx that = (EmployeeProfileXlsx) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
