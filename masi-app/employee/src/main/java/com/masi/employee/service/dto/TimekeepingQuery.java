package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@ParameterObject
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimekeepingQuery {
    private LocalDate startDate;
    private LocalDate endDate;
    private TimeKeepingType type;
    private WorkspaceType workSpaceTypes;
    private Pageable pageable;
    private String companyId;
    private Collection<UUID> workspaceIds;
    private String company;
}
