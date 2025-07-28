package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.InvoiceSupplies;
import com.masi.logistics.domain.Item;
import com.masi.logistics.service.dto.InvoiceSuppliesDTO;
import com.masi.logistics.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceSupplies} and its DTO {@link InvoiceSuppliesDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceSuppliesMapper extends EntityMapper<InvoiceSuppliesDTO, InvoiceSupplies> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    InvoiceSuppliesDTO toDto(InvoiceSupplies s);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    ItemDTO toItemId(Item item);
}
