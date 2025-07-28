package com.masi.sale.service.dto.reponse;

import com.masi.sale.domain.enumeration.StatusEntity;
import com.masi.sale.service.dto.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
public class DeliveryScheduleResponse {
    private String code;
    private ItemDTO item;
    private OrderDTO order;
    private ContractDTO contract;
    private BigDecimal needQuantity;
    private BigDecimal receivedQuantity;
    private BigDecimal remainingQuantity;
    private BigDecimal importPlan;
    private BigDecimal oweQuantity;
    private StatusEntity status;
    private Collection<DeliveryDetailDTO> deliverySchedules;
}
