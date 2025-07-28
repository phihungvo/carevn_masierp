package com.masi.sale.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
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
