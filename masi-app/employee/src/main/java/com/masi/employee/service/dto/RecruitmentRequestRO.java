package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.InterviewResult;
import com.masi.employee.domain.enumeration.Position;
import com.masi.employee.domain.enumeration.InterviewProcess;
import com.masi.employee.domain.enumeration.RecruitmentStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Data
public class RecruitmentRequestRO {
    private Collection<RecruitmentStatus> status;
    private Collection<Position> position;
    private String search;
    private String company;
    private LocalDate fromDate;
    private LocalDate toDate;
    private UUID departmentId;
}
