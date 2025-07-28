package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.SupplierContractDetail;
import com.masi.logistics.service.dto.ItemDTO;
import com.masi.logistics.service.dto.SupplierContractDTO;
import com.masi.logistics.service.dto.SupplierContractDetailDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SupplierContractDetail} and its DTO {@link SupplierContractDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierContractDetailMapper extends EntityMapper<SupplierContractDetailDTO, SupplierContractDetail> {
    @Mapping(target = "supplierContract", source = "supplierContract", qualifiedByName = "supplierContractId")
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    SupplierContractDetailDTO toDto(SupplierContractDetail s);

    @Named("supplierContractId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierContractDTO toDtoSupplierContractId(SupplierContract supplierContract);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = false)
    ItemDTO toDtoItemId(Item item);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
