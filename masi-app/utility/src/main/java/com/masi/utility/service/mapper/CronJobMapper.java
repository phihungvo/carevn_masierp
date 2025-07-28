package com.masi.utility.service.mapper;

import com.masi.utility.domain.CronJob;
import com.masi.utility.service.dto.CronJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CronJob} and its DTO {@link CronJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface CronJobMapper extends EntityMapper<CronJobDTO, CronJob> {}
