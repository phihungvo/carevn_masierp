package com.masi.production.service.mapper;

import com.masi.production.domain.SampleDisposal;
import com.masi.production.service.dto.SampleDisposalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SampleDisposal} and its DTO {@link SampleDisposalDTO}.
 */
@Mapper(componentModel = "spring")
public interface SampleDisposalMapper extends EntityMapper<SampleDisposalDTO, SampleDisposal> {}
