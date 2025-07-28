package com.masi.employee.service.mapper;

import com.masi.employee.domain.DocumentSequence;
import com.masi.employee.service.dto.DocumentSequenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentSequence} and its DTO {@link DocumentSequenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentSequenceMapper extends EntityMapper<DocumentSequenceDTO, DocumentSequence> {}
