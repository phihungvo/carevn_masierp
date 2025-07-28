package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.*;
import com.masi.logistics.service.dto.*;
import org.mapstruct.*;
import com.masi.logistics.service.dto.ContactDTO;

import java.util.Objects;
import java.util.UUID;

/**
 * Mapper for the entity {@link Inventories} and its DTO {@link InventoriesDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoriesMapper extends EntityMapper<InventoriesDTO, Inventories> {

    // Mapping for Inventories to InventoriesDTO
    @Mapping(target = "customer", source = "customer", qualifiedByName = "suppliersId")
    @Mapping(target = "customerRecipient", source = "customerRecipient", qualifiedByName = "suppliersId")
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "invoiceId")
    @Mapping(target = "incomingWarehouse", source = "incomingWarehouse", qualifiedByName = "warehouseId")
    @Mapping(target = "outgoingWarehouse", source = "outgoingWarehouse", qualifiedByName = "warehouseId")
    @Mapping(target = "supplierRequest", source = "supplierRequest", qualifiedByName = "suppliesRequestId")
    @Mapping(target = "inventoriesType", source = "inventoriesType", qualifiedByName = "inventoriesTypeId")
    @Mapping(target = "purchaseContract", source = "purchaseContract", qualifiedByName = "purchaseContractId")
    @Mapping(target = "totalQuantity", source = "totalQuantity")
    @Mapping(target = "isInvoice", source = "isInvoice")
    InventoriesDTO toDto(Inventories inventories);

    @Named("inventoriesTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    InventoriesTypeDTO toDtoInventoriesTypeId(InventoriesType inventoriesType);

    @Named("suppliersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    SuppliersDTO toDtoSuppliersId(Suppliers suppliers);

    @Named("invoiceId")
    @BeanMapping(ignoreByDefault = false)
    IncomingInvoice toDtoInvoiceId(IncomingInvoice invoiceSupplies);

    @Named("warehouseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    WarehouseDTO toDtoWarehouseId(Warehouse warehouse);

    @Named("suppliesRequestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "requestNumber", source = "requestNumber")
    SuppliesRequestDTO toDtoSuppliesRequestId(SuppliesRequest suppliesRequest);

    @Named("purchaseContractId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "contractCode", source = "contractCode")
    @Mapping(target = "contractName", source = "contractName")
    SupplierContractDTO toDtoContractId(SupplierContract contract);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }


    @Mapping(target = "warehouseGroupType", source = "warehouseGroupType")
    Inventories toEntity(InventoriesDTO inventoriesDTO);

}
