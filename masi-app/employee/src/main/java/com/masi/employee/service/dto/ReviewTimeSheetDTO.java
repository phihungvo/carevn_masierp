package com.masi.employee.service.dto;

import java.time.LocalDate;

import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;

import com.masi.employee.domain.enumeration.WorkspaceType;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ReviewTimeSheetDTO {
    private LocalDate   month;
    private TimesheetReviewStatus      status;
    private TimeKeepingType timeKeepingType = TimeKeepingType.HOUR;
    private String      note;
    private String signatureFile;
    private WorkspaceType workspaceType = WorkspaceType.OFFICE;

}
