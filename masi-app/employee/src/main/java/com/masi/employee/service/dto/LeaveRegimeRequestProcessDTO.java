package com.masi.employee.service.dto;

import com.masi.employee.domain.LeaveRegimeRequest;
import com.masi.employee.domain.ProcessLeaveRegimeRequest;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.ProcessLeaveRegimeRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the {@link com.masi.employee.domain.LeaveRegimeRequest} entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRegimeRequestProcessDTO implements Serializable {

    private String fileId;
    private String fileName;
    private String reason;
    private LeaveRegimeRequestProcessStatus status;

    public enum LeaveRegimeRequestProcessStatus {
        APPROVED,
        REJECTED
    }

    public void applyUpdateTo(ProcessLeaveRegimeRequest entity) {
        if (entity == null) {
            return;
        }
        entity.setUpdatedAt(ZonedDateTime.now());
        if (LeaveRegimeRequestProcessStatus.APPROVED.equals(status)) {
            entity.setStatus(ProcessLeaveRegimeRequestStatus.APPROVED);
            entity.setFileId(fileId);
            entity.setFileName(fileName);
        } else if (LeaveRegimeRequestProcessStatus.REJECTED.equals(status)) {
            entity.setStatus(ProcessLeaveRegimeRequestStatus.REJECTED);
            entity.setReason(reason);
        }
    }
}
