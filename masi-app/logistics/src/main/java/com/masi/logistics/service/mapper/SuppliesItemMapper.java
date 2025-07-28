package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.SupplierDetail;
import com.masi.logistics.domain.Suppliers;
import com.masi.logistics.domain.SuppliesItem;
import com.masi.logistics.service.dto.ItemDTO;
import com.masi.logistics.service.dto.SupplierDetailDTO;
import com.masi.logistics.service.dto.SuppliersDTO;
import com.masi.logistics.service.dto.SuppliesItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SuppliesItem} and its DTO {@link SuppliesItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface SuppliesItemMapper extends EntityMapper<SuppliesItemDTO, SuppliesItem> {

    @Mapping(target = "uomDTO", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierId")
    SuppliesItemDTO toDto(SuppliesItem s);

    @Named("itemId")
    ItemDTO toDtoItem(Item item);

    @Named("supplierId")
    SuppliersDTO toDtoSupplier(Suppliers suppliers);
}
