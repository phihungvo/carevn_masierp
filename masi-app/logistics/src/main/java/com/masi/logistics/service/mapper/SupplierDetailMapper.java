package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.SupplierDetail;
import com.masi.logistics.domain.Uom;
import com.masi.logistics.service.dto.ItemDTO;
import com.masi.logistics.service.dto.SupplierDetailDTO;
import com.masi.logistics.service.dto.UomDTO;
import org.mapstruct.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Mapper for the entity {@link SupplierDetail} and its DTO {@link SupplierDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierDetailMapper extends EntityMapper<SupplierDetailDTO, SupplierDetail> {
    // mapstruct will generate the implementation of this method
    // and it will be used in the service layer

    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    SupplierDetailDTO toDto(SupplierDetail s);

    @Named("itemId")
    ItemDTO toDtoItem(Item item);


    default String map(UUID value) {
        return Objects.toString(value, null);
    }


}
