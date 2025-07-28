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
public class ExplanationRequestObjectBase extends RequestObjectBase {
    private UUID explanationId;
    protected UUID reviewerId;

    public ExplanationRequestObjectBase(UUID id, UUID reviewerId, UUID explanationId, List<String> statuses, String searchString) {
        super(id, statuses, searchString);
        this.reviewerId = reviewerId;
        this.explanationId = explanationId;
    }

    public ExplanationRequestObjectBase() {
    }

}
