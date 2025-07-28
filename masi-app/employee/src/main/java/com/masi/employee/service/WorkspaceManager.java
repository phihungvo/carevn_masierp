package com.masi.employee.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkspaceManager {
    private static final Logger log = LoggerFactory.getLogger(WorkspaceManager.class);

    private final WorkspaceService workspaceService;
    private final EmployeeService employeeService;

    public WorkspaceManager(WorkspaceService workspaceService, EmployeeService employeeService) {
        this.workspaceService = workspaceService;
        this.employeeService = employeeService;
    }
}
