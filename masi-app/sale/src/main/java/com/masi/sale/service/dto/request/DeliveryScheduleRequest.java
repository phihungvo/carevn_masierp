package com.masi.sale.service.dto.request;

import com.masi.sale.domain.enumeration.StatusEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DeliveryScheduleRequest {
    private LocalDate deliveryDate;
    private LocalDate expectedReceiveDate;
    private StatusEntity status;

}
