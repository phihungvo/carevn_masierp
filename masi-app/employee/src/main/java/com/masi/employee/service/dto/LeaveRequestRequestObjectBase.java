package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.ReviewStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeaveRequestRequestObjectBase extends RequestObjectBase {
    private UUID leaveRequestId;
    private UUID reviewerId;

    public LeaveRequestRequestObjectBase(UUID id, UUID reviewerId, UUID leaveRequestId, List<String> statuses, String searchString) {
        super(id, statuses, searchString);
        this.reviewerId = reviewerId;
        this.leaveRequestId = leaveRequestId;
    }

    public LeaveRequestRequestObjectBase() {
    }

}
