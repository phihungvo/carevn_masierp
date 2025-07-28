package com.masi.employee.service.dto;

import com.masi.employee.domain.Workspace;
import com.masi.employee.domain.enumeration.EmployeeStatus;
import com.masi.employee.domain.enumeration.ProfileState;
import com.masi.employee.domain.enumeration.WorkspaceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ParameterObject
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfileQuery implements Serializable {
    private Collection<ProfileState> profileStates;
    private String search;
    private Collection<UUID> workspaceIds;
    private Collection<EmployeeStatus> employeeStatuses;
    private String department;
    private String company;
    private Boolean isFilterCompany = true;
    private Collection<WorkspaceType> workspaceTypes;
    private Boolean hasAccount = null;
    private Collection<String> workspaceNNames;

    public Boolean getIsActive() {
        if (profileStates == null || profileStates.isEmpty()) {
            return null;
        }
        if (profileStates.contains(ProfileState.ACTIVE) && profileStates.contains(ProfileState.INACTIVE)) {
            return null;
        }
        return profileStates.contains(ProfileState.ACTIVE);
    }
}
