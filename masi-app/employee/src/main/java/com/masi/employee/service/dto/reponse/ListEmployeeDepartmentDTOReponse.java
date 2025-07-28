package com.masi.employee.service.dto.reponse;

import com.masi.employee.service.dto.EmployeeProfileDTO;
import lombok.Data;

import java.util.Collection;

@Data
public class ListEmployeeDepartmentDTOReponse {
    private Collection<EmployeeProfileDTO> employeesHCNS;
    private Collection<EmployeeProfileDTO> employeesSALE;
    private Collection<EmployeeProfileDTO> employeesWORKER;
    private Collection<EmployeeProfileDTO> employeesLOGPUR;
}
