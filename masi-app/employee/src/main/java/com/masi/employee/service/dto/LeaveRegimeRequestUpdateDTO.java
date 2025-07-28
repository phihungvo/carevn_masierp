package com.masi.employee.service.dto;

import com.masi.employee.domain.EmbedFile;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.ProcessLeaveRegimeRequestStatus;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.LeaveRegimeRequest} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRegimeRequestUpdateDTO implements Serializable {

    @NotNull(message = "must not be null")
    private LeaveType leaveType;

    @NotNull(message = "must not be null")
    private ZonedDateTime lastWorkDate;

    @NotNull(message = "must not be null")
    private ZonedDateTime returnWorkDate;

    private ZonedDateTime fromTime;

    private ZonedDateTime toTime;

    private Collection<EmbedFile> files;

    private UUID substituteId;

    private String fileId;

    private String fileName;

    private UUID employeeId;

    private ArrayList<UUID> approverIds = new ArrayList<>();

    private LeaveRequestDayType leaveRequestDayType;
    private Float totalDayOff;

    public LeaveRegimeRequestDTO toDto(UUID id) {
        var leaveRegimeRequestDTO = new LeaveRegimeRequestDTO();
        leaveRegimeRequestDTO.setId(id);
        leaveRegimeRequestDTO.setLeaveType(this.leaveType);
        leaveRegimeRequestDTO.setLastWorkDate(this.lastWorkDate);
        leaveRegimeRequestDTO.setReturnWorkDate(this.returnWorkDate);
        leaveRegimeRequestDTO.setSubstituteId(this.substituteId);
        leaveRegimeRequestDTO.setEmployeeId(this.employeeId);
        leaveRegimeRequestDTO.setLeaveRequestDayType(this.leaveRequestDayType);
        leaveRegimeRequestDTO.setFileId(this.fileId);
        leaveRegimeRequestDTO.setFileName(this.fileName);
        leaveRegimeRequestDTO.setEmbedFiles(this.files);
        leaveRegimeRequestDTO.setFromTime(this.fromTime);
        leaveRegimeRequestDTO.setToTime(this.toTime);
        leaveRegimeRequestDTO.setTotalDayOff(this.totalDayOff);
        return leaveRegimeRequestDTO;
    }

}
