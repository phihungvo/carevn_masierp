package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Contact;
import com.masi.logistics.domain.ContactGift;
import com.masi.logistics.service.dto.ContactDTO;
import com.masi.logistics.service.dto.ContactGiftDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactGift} and its DTO {@link ContactGiftDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactGiftMapper extends EntityMapper<ContactGiftDTO, ContactGift> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactId")
    ContactGiftDTO toDto(ContactGift s);

    @Named("contactId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ContactDTO toDtoContactId(Contact contact);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
