package com.masi.production.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDTO implements Serializable {

    private UUID id;

    private String firstName;

    private String lastName;

    private Boolean result;

    public String getFullName() {
        if (this.firstName == null || this.lastName == null) {
            return null;
        }
        return this.lastName + " " + this.firstName;
    }
}
