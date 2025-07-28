package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import lombok.Data;

import java.util.UUID;

@Data
public class ExplanationRequest {
    private UUID id;
    private String explanation;
    private TimeKeepingViolationType reason;
}
