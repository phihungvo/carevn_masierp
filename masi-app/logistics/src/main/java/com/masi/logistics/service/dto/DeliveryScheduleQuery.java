package com.masi.logistics.service.dto;

import com.masi.logistics.domain.DeliverySchedule;
import com.masi.logistics.domain.criteria.DeliveryScheduleCriteria;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@ParameterObject
public class DeliveryScheduleQuery {
    private String search;
    private List<DeliverySchedule.Status> statuses;
    private List<UUID> orderIds;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate startReceiveDate;
    private LocalDate endReceiveDate;

    public DeliveryScheduleCriteria toCriteria() {
        var criteria = new DeliveryScheduleCriteria();
        if (statuses != null && !statuses.isEmpty())
            criteria.status().setIn(statuses);
        if (StringUtils.isNotBlank(search)) {
            criteria.code().setContains(search);
        }
        if (orderIds != null && !orderIds.isEmpty())
            criteria.orderId().setIn(orderIds);
        if (startDate != null)
            criteria.deliveryDate().setGreaterThanOrEqual(startDate);
        if (endDate != null)
            criteria.deliveryDate().setLessThanOrEqual(endDate);
        if (startReceiveDate != null)
            criteria.expectedReceiveDate().setGreaterThanOrEqual(startReceiveDate);
        if (endReceiveDate != null)
            criteria.expectedReceiveDate().setLessThanOrEqual(endReceiveDate);
        return criteria;
    }
}
