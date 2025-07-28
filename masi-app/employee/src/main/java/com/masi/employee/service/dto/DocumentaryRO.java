package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.DocumentaryGroup;
import com.masi.employee.domain.enumeration.DocumentaryType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DocumentaryRO {

    private LocalDate documentDateFrom;
    private LocalDate documentDateTo;


    private LocalDate documentDate;

    private DocumentaryType documentaryType;

    private DocumentaryGroup documentaryGroup;

    private String search;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String companyId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID employeeId;

}
