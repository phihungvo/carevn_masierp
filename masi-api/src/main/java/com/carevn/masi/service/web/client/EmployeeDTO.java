package com.carevn.masi.service.web.client;

import com.carevn.masi.service.dto.WorkspaceDTO;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;


@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDTO implements Serializable {

    private UUID id;

    private String firstName;

    private String lastName;

    private Boolean result;
    private WorkspaceDTO workspace;
    public String getFullName() {
        if (this.firstName == null || this.lastName == null) {
            return null;
        }
        return this.lastName + " " + this.firstName;
    }

}
