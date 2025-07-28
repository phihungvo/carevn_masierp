package com.masi.logistics.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovedRequest {

    private String approvalSignId;
    private String approvalSignName;
    private String rejectNote;
}
