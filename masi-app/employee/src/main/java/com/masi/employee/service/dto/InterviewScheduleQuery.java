package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.InterviewProcess;
import com.masi.employee.domain.enumeration.InterviewResult;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

import java.util.Collection;
import java.util.UUID;

@Data
@ParameterObject
public class InterviewScheduleQuery {
    private Collection<InterviewResult> interviewResult;
    private String search;
    private Collection<InterviewProcess> process;
    private String recruitmentId;
    private String company;
}
