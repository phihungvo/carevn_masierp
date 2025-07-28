package com.masi.sale.service.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestReviewDTO implements Serializable {
    @NotNull(message = "must not be null")
    private UUID documentId;
    private UUID employeeId1;
    private UUID employeeId2;
    private UUID employeeId3;
    private UUID employeeId4;
    private UUID employeeId5;
    private UUID employeeId6;
    private UUID employeeId7;
    private UUID employeeId8;

    @JsonIgnore
    public Collection<UUID> getEmployeeIds() {
        return Stream.of(employeeId1, employeeId2, employeeId3, employeeId4, employeeId5, employeeId6, employeeId7, employeeId8).filter(Objects::nonNull).collect(Collectors.toSet());
    }

}
