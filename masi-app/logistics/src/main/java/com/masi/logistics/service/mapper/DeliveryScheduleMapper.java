package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.DeliverySchedule;
import com.masi.logistics.service.dto.DeliveryScheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliverySchedule} and its DTO {@link DeliveryScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeliveryScheduleMapper extends EntityMapper<DeliveryScheduleDTO, DeliverySchedule> {}
