package com.masi.employee.service.dto;

import com.masi.employee.domain.EmbedFile;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

/**
 * A DTO for the {@link com.masi.employee.domain.LeaveRegimeRequest} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRegimeRequestCreateDTO implements Serializable {

    @NotNull(message = "must not be null")
    private LeaveType leaveType;

    @NotNull(message = "must not be null")
    private ZonedDateTime lastWorkDate;

    @NotNull(message = "must not be null")
    private ZonedDateTime returnWorkDate;

    private ZonedDateTime fromTime;

    private ZonedDateTime toTime;

    private UUID substituteId;

    private ArrayList<UUID> approverIds = new ArrayList<>();

    private UUID employeeId;

    private String fileId;

    private String fileName;
    private Collection<EmbedFile> files;

    private LeaveRequestDayType leaveRequestDayType;

    private Float totalDayOff;

    public LeaveRegimeRequestDTO toDto() {
        var leaveRegimeRequestDTO = new LeaveRegimeRequestDTO();
        leaveRegimeRequestDTO.setId(UUID.randomUUID());
        leaveRegimeRequestDTO.setLeaveType(this.leaveType);
        leaveRegimeRequestDTO.setLastWorkDate(this.lastWorkDate);
        leaveRegimeRequestDTO.setReturnWorkDate(this.returnWorkDate);
        leaveRegimeRequestDTO.setSubstituteId(this.substituteId);
        leaveRegimeRequestDTO.setEmployeeId(this.employeeId);
        leaveRegimeRequestDTO.setLeaveRequestDayType(this.leaveRequestDayType);
        leaveRegimeRequestDTO.setIsDeleted(false);
        leaveRegimeRequestDTO.setStatus(LeaveRegimeRequestStatus.WAITING_APPROVAL);
        leaveRegimeRequestDTO.setCreatedAt(ZonedDateTime.now());
        leaveRegimeRequestDTO.setFileId(fileId);
        leaveRegimeRequestDTO.setFileName(fileName);
        leaveRegimeRequestDTO.setEmbedFiles(files);
        leaveRegimeRequestDTO.setFromTime(this.fromTime);
        leaveRegimeRequestDTO.setToTime(this.toTime);
        leaveRegimeRequestDTO.setTotalDayOff(this.totalDayOff);
        return leaveRegimeRequestDTO;
    }

}
