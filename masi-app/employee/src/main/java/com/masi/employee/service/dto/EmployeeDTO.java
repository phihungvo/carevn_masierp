package com.masi.employee.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.Employee} entity.
 */
@Setter
@Getter
@Schema(description = "The Employee entity.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1324324324L;

    private UUID id;

    private String code;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private ZonedDateTime hireDate;

    private Long salary;

    private Long commissionPct;

    private List<TimeKeepingDTO> timeKeepings;

    private WorkspaceDTO workspace;

    private UUID workspaceId;
    private EmployeeProfileDTO employeeProfile;
    private String employeeCode;


    private String workspaceName;

    private String workspaceNormalizedName;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO employeeDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id='" + getId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            ", salary=" + getSalary() +
            ", commissionPct=" + getCommissionPct() +
            "}";
    }


}
