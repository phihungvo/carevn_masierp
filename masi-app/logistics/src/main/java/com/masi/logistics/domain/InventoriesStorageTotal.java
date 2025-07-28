package com.masi.logistics.domain;

import com.masi.logistics.service.dto.InventoriesStorageDTO;
import com.masi.logistics.service.dto.ItemCategoryDTO;
import lombok.Data;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class InventoriesStorageTotal extends InventoriesStorage implements Serializable, Persistable<UUID> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column("total_count")
    private BigDecimal totalQuantity;

    @Column("item_code")
    private String itemCode;

    @Column("item_name")
    private String itemName;

    @Column("item_id")
    private UUID itemId;

    @Column("uom_id")
    private String uomId;

    @Column("uom_name")
    private String uomName;

    @Column("item_category_id")
    private UUID itemCategoryId;

    @Column("item_category_name")
    private String itemCategoryName;

    @Column("item_category_code")
    private String itemCategoryCode;


    public InventoriesStorageDTO toDto() {
        InventoriesStorageDTO dto = super.toDto();
        dto.setTotalQuantity(this.getTotalQuantity());
        dto.setItemCode(this.getItemCode());
        dto.setItemId(this.getItemId());
        dto.setItemName(this.getItemName());
        dto.setUomId(this.getUomId());
        dto.setUomName(this.getUomName());
        return dto;
    }

}
