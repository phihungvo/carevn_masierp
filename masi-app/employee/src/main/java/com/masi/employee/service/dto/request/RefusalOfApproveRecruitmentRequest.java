package com.masi.employee.service.dto.request;

import jakarta.validation.constraints.NotNull;

public class RefusalOfApproveRecruitmentRequest {

    @NotNull(message = "must not be null")
    public String rejectNote;

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }
}
