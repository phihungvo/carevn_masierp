package com.carevn.masi.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class EmployeeDTO  implements Serializable {

    private UUID id;

    private String firstName;

    private String lastName;
    private WorkspaceDTO workspace;
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public WorkspaceDTO getWorkspace() {
        return workspace;
    }
    public void setWorkspace(WorkspaceDTO workspace) {
        this.workspace = workspace;
    }
    public String toString() {
        return "EmployeeDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", workspace=" + workspace + "]";
    }
    
}
