package com.masi.sale.service.mapper;

import com.masi.sale.domain.DeliverySchedule;
import com.masi.sale.service.dto.DeliveryScheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliverySchedule} and its DTO {@link DeliveryScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeliveryScheduleMapper extends EntityMapper<DeliveryScheduleDTO, DeliverySchedule> {}
