package com.carevn.masi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
@NoArgsConstructor
public class EmployeeProfileDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String employeeCode;

    private Boolean isHasProfileAttachment;

    private String fullName;

    public String getFullName() {
        if (this.fullName == null) {
            return "";
        }
        return this.fullName;
    }

    private UUID workspaceId;

    private String citizenId;

    private LocalDate citizenIssueDate;

    private String citizenIssuePlace;

    private String residenceAddress;

    private String temporaryAddress;

    private LocalDate birthday;

    private String phone;

    private String taxCode = "";



    private String level;

    private String parkingCard;

    private String insuranceCard;

    private UUID referrerId;

    private LocalDate referrerDate;

    private String email;

    private String note;

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


    public EmployeeDTO toEmployeeDto(){
        var employeeDTO = new EmployeeDTO();
        employeeDTO.setId(this.id);
        employeeDTO.setEmployeeCode(this.employeeCode);
        employeeDTO.setFullName(this.fullName);
        employeeDTO.setWorkspaceName(this.workspace.getName());
        employeeDTO.setWorkspaceNormalizedName(this.workspace.getNormalizedName());
        return employeeDTO;
    }

    public List<EmployeeDTO> toListEmployeeDto(List<EmployeeProfileDTO> listEmployeeProfileDTO){
        var listEmployeeDTO = new ArrayList<EmployeeDTO>();
        if (listEmployeeProfileDTO != null) {
            for (EmployeeProfileDTO employeeProfileDTO : listEmployeeProfileDTO) {
                listEmployeeDTO.add(employeeProfileDTO.toEmployeeDto());
            }
        }
        return listEmployeeDTO;
    }
}
