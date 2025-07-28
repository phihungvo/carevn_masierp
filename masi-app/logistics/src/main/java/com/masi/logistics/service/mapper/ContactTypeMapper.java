package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.ContactType;
import com.masi.logistics.service.dto.ContactTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactType} and its DTO {@link ContactTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactTypeMapper extends EntityMapper<ContactTypeDTO, ContactType> {}
