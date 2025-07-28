package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.ExplanationStatus;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@Data
@ParameterObject
public class TimeKeepingExplanationQuery {

    private List<ExplanationStatus> status;
    private List<TimeKeepingViolationType> type;

}
