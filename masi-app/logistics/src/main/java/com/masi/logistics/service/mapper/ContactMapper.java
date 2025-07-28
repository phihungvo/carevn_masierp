package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Contact;
import com.masi.logistics.domain.ContactType;
import com.masi.logistics.service.dto.ContactDTO;
import com.masi.logistics.service.dto.ContactTypeDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "contactType", source = "contactType", qualifiedByName = "contactTypeId")
    ContactDTO toDto(Contact s);

    @Named("contactTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ContactTypeDTO toDtoContactTypeId(ContactType contactType);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
