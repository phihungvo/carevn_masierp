package com.masi.logistics.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialMixDTO {

    @Column("item_id")
    private UUID itemId;

    @Column("item_name")
    private String itemName;

    @Column("item_category_id")
    private UUID itemCategoryId;

    @Column("item_category_code")
    private String itemCategoryCode;

    @Column("item_category_name")
    private String itemCategoryName;

    @Column("item_code")
    private String itemCode;

    @Column("quantity")
    private Integer quantity;

    @Column("uom_id")
    private UUID uomId;

    @Column("uom_name")
    private String uomName;

    @Column("expire_date")
    private LocalDate expireDate;

    @Column("warehouse_id")
    private UUID warehouseId;

    @Column("warehouse_name")
    private String warehouseName;

    @Column("warehouse_type_id")
    private UUID warehouseTypeId;

    @Column("warehouse_type_name")
    private String warehouseTypeName;

    @Column("percent_protein")
    private Float percentProtein;

}



