package com.masi.sale.service.dto.request;

import com.masi.sale.domain.enumeration.ContractStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ApprovedContractRequest {
    private ContractStatus contractStatus;
    private String approvalSign;
    private String rejectNote;
    private String approvalSignName;
    private UUID reviewId;

}
