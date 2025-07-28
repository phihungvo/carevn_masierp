package com.masi.sale.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovedRequest {

    private String approvalSignId;
    private String rejectNote;
    private Boolean result;

}
