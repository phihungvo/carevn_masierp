package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.logistics.domain.Warehouse;
import com.masi.logistics.domain.enumeration.WarehouseTypePage;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.Warehouse} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarehouseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    private String name;

    private String address;

    private Boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updateAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deleteAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deleteBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    private WarehouseTypeDTO warehouseType = new WarehouseTypeDTO();

    private UUID warehouseTypeId = UUID.randomUUID();

    private WarehouseTypePage warehouseTypePage;

    public Warehouse toEntity(){
        Warehouse warehouse = new Warehouse();
        warehouse.setId(this.id);
        warehouse.setCode(this.code);
        warehouse.setName(this.name);
        warehouse.setAddress(this.address);
        warehouse.setActive(this.active);
        warehouse.setCreateAt(this.createAt);
        warehouse.setCreateBy(this.createBy);
        warehouse.setUpdateAt(this.updateAt);
        warehouse.setUpdateBy(this.updateBy);
        warehouse.setDeleteAt(this.deleteAt);
        warehouse.setDeleteBy(this.deleteBy);
        warehouse.setCompany(this.company);
        warehouse.setWarehouseTypeId(this.warehouseType.getId() != null ? this.warehouseType.getId() : this.warehouseTypeId);
        warehouse.setWarehouseTypePage(this.warehouseTypePage);
        System.out.println("warehouse type id");
        System.out.println(this.warehouseType.toString());
        System.out.println(this.getWarehouseTypeId());
        return warehouse;
    }
}


